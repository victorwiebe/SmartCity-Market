package ml.extractor.dataminers;

import java.time.LocalDate;
import java.time.Period;
import java.util.Comparator;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.Map.Entry;
import java.util.function.ToDoubleFunction;
import java.util.stream.Collectors;

import api.contracts.IGroceryList;
import api.contracts.IGroceryPackage;
import api.contracts.IProduct;
import api.preferences.InputPreferences;
import api.types.StoreData;
import ml.common.property.basicproperties.ABasicProperty;
import ml.common.property.basicproperties.storestatistics.AverageAmountOfProductForCustomerProperty;
import ml.common.property.basicproperties.storestatistics.LastPopularProductOfCustomerProperty;
import ml.common.property.basicproperties.storestatistics.MostPopularProductOfCustomerProperty;
import ml.common.property.basicproperties.storestatistics.NumOfBuyersPerMonthProperty;
import ml.common.property.basicproperties.storestatistics.NumOfCustomerPurchasesPerMonthProperty;
import ml.common.property.basicproperties.storestatistics.SumOfCustomerPurchasesPerMonthProperty;

/**
 * This class extarcts basic properties from user
 *  
 * @author noam
 *
 */
public class CustomerMiner extends AMiner {

	public CustomerMiner(InputPreferences inputPreferences, StoreData storeData, IGroceryList currentGrocery,
			IProduct currentProduct) {
		super(inputPreferences, storeData, currentGrocery, currentProduct);
	}
	
	@Override
	public Set<ABasicProperty> extractProperties() {
		Set<ABasicProperty> result = new HashSet<>(extractMostPopularCustomerProducts());

		result.addAll(extractLastPopularCustomerProducts());
		result.addAll(extractNumberOfCustomerPurchasesPerMonth());
		result.addAll(extractSumOfCustomerPurcahsesPerMonth());
		result.addAll(extractAverageAmountOfProductOfCustomer());
		
		return result;
	}
	
	/**
	 * this methods generate property of the most popular items of the customer (the number of
	 * top popular item declared in {@link MostPopularProductOfCustomerProperty}
	 * 
	 * @return
	 */
	private Set<? extends ABasicProperty> extractMostPopularCustomerProducts() {

		Map<? extends IProduct, Long> productsCount = getHistory().stream()
				.filter(g -> getCurrentGrocery().getBuyer().equals(g.getBuyer()))
				.flatMap(g -> g.getProductsList().stream())
				.collect(Collectors.groupingBy(g -> ((IGroceryPackage) g).getProduct(), Collectors.counting()));

		List<MostPopularProductOfCustomerProperty> ProductsOrederdByPopularity = productsCount.entrySet().stream()
				.sorted(new Comparator<Entry<? extends IProduct, Long>>() {

					@Override
					public int compare(Entry<? extends IProduct, Long> arg0, Entry<? extends IProduct, Long> arg1) {
						return Long.compare(arg1.getValue(), arg0.getValue());
					}
				}).map(e -> new MostPopularProductOfCustomerProperty(e.getKey(), e.getValue())).collect(Collectors.toList());

		return new HashSet<>(ProductsOrederdByPopularity.subList(0, 
				Math.min(MostPopularProductOfCustomerProperty.numOfTop, ProductsOrederdByPopularity.size())));

	}
	
	/**
	 * this methods generate property of the last popular items of the customer (the number of
	 * last popular item declared in {@link LastPopularProductOfCustomerProperty}
	 * 
	 * @return
	 */
	private Set<? extends ABasicProperty> extractLastPopularCustomerProducts() {

		Map<? extends IProduct, Long> productsCount = getHistory().stream()
				.filter(g -> getCurrentGrocery().getBuyer().equals(g.getBuyer()))
				.flatMap(g -> g.getProductsList().stream())
				.collect(Collectors.groupingBy(g -> ((IGroceryPackage) g).getProduct(), Collectors.counting()));

		List<LastPopularProductOfCustomerProperty> ProductsOrederdByPopularity = productsCount.entrySet().stream()
				.sorted(new Comparator<Entry<? extends IProduct, Long>>() {

					@Override
					public int compare(Entry<? extends IProduct, Long> arg0, Entry<? extends IProduct, Long> arg1) {
						return -Long.compare(arg1.getValue(), arg0.getValue());
					}
				}).map(e -> new LastPopularProductOfCustomerProperty(e.getKey(), e.getValue())).collect(Collectors.toList());

		return new HashSet<>(ProductsOrederdByPopularity.subList(0, 
				Math.min(LastPopularProductOfCustomerProperty.numOfBottom, ProductsOrederdByPopularity.size())));

	}
	
	/**
	 * this methods generate property of the number of purchases of customer in given month {@link NumOfBuyersPerMonthProperty}
	 * the limit of monthes back to look at is defined in {@link NumOfCustomerPurchasesPerMonthProperty}
	 * 
	 * @return
	 */
	private Set<? extends ABasicProperty> extractNumberOfCustomerPurchasesPerMonth() {

		LocalDate currentDate = LocalDate.now();

		Map<Integer, Long> numBuyersPerMonthMap = 
				getHistory().stream()
					.filter(gl -> getCurrentGrocery().getBuyer().equals(gl.getBuyer()))
					.collect(Collectors.groupingBy( (IGroceryList t) -> Period.between(t.getPurchaseDate(), currentDate).getMonths(), Collectors.counting()));

		Set<NumOfCustomerPurchasesPerMonthProperty> result = new HashSet<>();
		
		for (int i = 0; i < NumOfCustomerPurchasesPerMonthProperty.goMonthesBackLimit; ++i)
			result.add(new NumOfCustomerPurchasesPerMonthProperty(i, 
					(int) (!numBuyersPerMonthMap.containsKey(i) ? 0 : numBuyersPerMonthMap.get(i))));
		
		return result;
	}
	
	/**
	 * this methods generate property of the sum of purchases of the customer in given month {@link SumOfCustomerPurchasesPerMonthProperty}
	 * the limit of monthes back to look at is defined in {@link SumOfCustomerPurchasesPerMonthProperty}
	 * 
	 * @return
	 */
	private Set<? extends ABasicProperty> extractSumOfCustomerPurcahsesPerMonth() {

		LocalDate currentDate = LocalDate.now();

		Map<Integer, List<IGroceryList>> purchasesPerMonthMap = 
				getHistory().stream()
				.filter(gl -> getCurrentGrocery().getBuyer().equals(gl.getBuyer()))
				.map(gl -> (IGroceryList)gl)
					.collect(Collectors.groupingBy( (IGroceryList t) -> Period.between(t.getPurchaseDate(), currentDate).getMonths()));
		
		Map<Integer, Double> sumPerMonthMap = new HashMap<>();
		for (Integer i : purchasesPerMonthMap.keySet())
			sumPerMonthMap.put(i, purchasesPerMonthMap.get(i).stream().flatMap(gl -> gl.getProductsList().stream())
					.collect(Collectors.summingDouble(new ToDoubleFunction<IGroceryPackage>() {
						@Override
						public double applyAsDouble(IGroceryPackage value) {
							return value.getAmount() * value.getProduct().getPrice();
						}
					})));

		Set<SumOfCustomerPurchasesPerMonthProperty> result = new HashSet<>();
		
		for (int i = 0; i < SumOfCustomerPurchasesPerMonthProperty.goMonthesBackLimit; ++i)
			result.add(new SumOfCustomerPurchasesPerMonthProperty(i, 
					 (!sumPerMonthMap.containsKey(i) ? 0 : sumPerMonthMap.get(i))));
		
		return result;
	}	
	
	/**
	 * this methods generate property of the sum of purchases of the customer in given month {@link SumOfCustomerPurchasesPerMonthProperty}
	 * the limit of monthes back to look at is defined in {@link SumOfCustomerPurchasesPerMonthProperty}
	 * 
	 * @return
	 */
	private Set<? extends ABasicProperty> extractAverageAmountOfProductOfCustomer() {

		Set<AverageAmountOfProductForCustomerProperty> result = new HashSet<>();
		
		
		
		Set<IProduct> allProduct = getHistory().stream()
				.filter(gl -> gl.getBuyer().equals(getCurrentGrocery().getBuyer()))
				.flatMap(gl -> gl.getProductsList().stream())
				.map((IGroceryPackage p) -> p.getProduct())
				.distinct()
				.collect(Collectors.toSet());
		
		for (IProduct iProduct : allProduct) {
			
			long num = getHistory().stream().filter(gl -> gl.getBuyer().equals(getCurrentGrocery().getBuyer()))
					.filter(gl -> {
						for (IGroceryPackage pack : gl.getProductsList())
							if (pack.getProduct().equals(iProduct))
								return true;
						return false;
					}).count();
			
			result.add(new AverageAmountOfProductForCustomerProperty(iProduct, num == 0 ? 0
					: 1. * getHistory().stream().filter(gl -> gl.getBuyer().equals(getCurrentGrocery().getBuyer()))
							.flatMap(gl -> gl.getProductsList().stream()).filter(p -> p.getProduct().equals(iProduct))
							.collect(Collectors.summingLong(p -> p.getAmount())) / num));
		}

		return result;
	}
	
	

}
