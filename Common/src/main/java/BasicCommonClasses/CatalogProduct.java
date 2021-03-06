package BasicCommonClasses;
/*
 * @author Lior Ben Ami
 */

import java.util.HashSet;
import api.contracts.IProduct;

/** CatalogProduct - The info of a product of the market's catalog. 
 * @author Lior Ben Ami
 * @since 2016-12-09 */
public class CatalogProduct implements IProduct {
	long barcode;
	String name;
	HashSet<Ingredient> ingredients;
	Manufacturer manufacturer;
	String description;
	double price;
	String imageUrl;
	HashSet<Location> locations;
	Sale sale;
	Sale specialSale;
	
	public CatalogProduct(long barcode, String name, HashSet<Ingredient> ingredients, Manufacturer manufacturer,
			String description, double price, String imageUrl, HashSet<Location> locations) {
		this.barcode = barcode;
		this.name = name;
		this.ingredients = ingredients;
		this.manufacturer = manufacturer;
		this.description = description;
		this.price = price;
		this.imageUrl = imageUrl;
		this.locations = locations;
		this.sale = new Sale();
		this.specialSale = new Sale();
	}

	/* Empty constructor - need for using map */
	public CatalogProduct() {
	}

	@Override
	public long getBarcode() {
		return barcode;
	}

	public void setBarcode(long barcode) {
		this.barcode = barcode;
	}

	@Override
	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	@Override
	public HashSet<Ingredient> getIngredients() {
		return ingredients;
	}

	public void setIngredients(HashSet<Ingredient> ¢) {
		this.ingredients = ¢;
	}

	public void setManufacturer(Manufacturer ¢) {
		this.manufacturer = ¢;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	@Override
	public double getPrice() {
		return price;
	}

	public void setPrice(double price) {
		this.price = price;
	}

	public String getImageUrl() {
		return imageUrl;
	}

	public void setImageUrl(String ¢) {
		this.imageUrl = ¢;
	}


	public HashSet<Location> getLocations() {
		return locations;
	}

	public void setLocations(HashSet<Location> ¢) {
		this.locations = ¢;
	}
		
	public void addIngredient(Ingredient ¢) {
		if (ingredients == null)
			ingredients = new HashSet<Ingredient>();
		ingredients.add(¢);
	}
	
	public void removeIngredient(Ingredient ¢) {
		if (ingredients != null)
			ingredients.remove(¢);
	}
	
	public void addLocation(Location ¢) {
		if (locations == null)
			locations = new HashSet<Location>();
		locations.add(¢);
	}
	
	public void addLocation (int passage, int column, PlaceInMarket m) {
		if (locations == null)
			locations = new HashSet<Location>();
		locations.add(new Location(passage, column, m));
	}
	
	public void removeLocation(Location ¢) {
		if (locations != null)
			locations.remove(¢);
	}
	
	public void removeLocation (int passage, int column, PlaceInMarket m) {
		if (locations != null)
			locations.remove(new Location(passage, column, m));
	}
	
	public Sale getSale() {
		return sale;
	}

	public void setSale(Sale s) {
		this.sale = s;
	}
	
	public Sale getSpecialSale() {
		return specialSale;
	}

	public void setSpecialSale(Sale specialSale) {
		this.specialSale = specialSale;
	}

	@Override
	public int hashCode() {
		return (int) (barcode ^ (barcode >>> 32)) + 31;
	}

	@Override
	public boolean equals(Object ¢) {
		return ¢ == this || (¢ != null && getClass() == ¢.getClass() && barcode == ((CatalogProduct) ¢).barcode);
	}	
	
	public boolean isValid() {
		return (barcode >= 0) && (!"".equals(name)) &&
				(name != null) && (price > 0);
	}

	@Override
	public Manufacturer getManufacturer() {
		return manufacturer;
	}

	@Override
	public double getNormalizeDistanceFrom(IProduct other) {
		return 0;
	}
}
