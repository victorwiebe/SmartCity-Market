package CommandHandlerTest;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.fail;

import org.apache.log4j.PropertyConfigurator;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.runners.MockitoJUnitRunner;

import com.google.gson.Gson;

import BasicCommonClasses.CatalogProduct;
import ClientServerApi.CommandDescriptor;
import ClientServerApi.CommandWrapper;
import ClientServerApi.ResultDescriptor;
import CommandHandler.CommandExecuter;
import SQLDatabase.SQLDatabaseConnection;
import SQLDatabase.SQLDatabaseException.CriticalError;
import SQLDatabase.SQLDatabaseException.IngredientNotExist;
import SQLDatabase.SQLDatabaseException.ManufacturerNotExist;
import SQLDatabase.SQLDatabaseException.ProductAlreadyExistInCatalog;
import SQLDatabase.SQLDatabaseException.ClientNotConnected;

/**
 * @author Aviad Cohen
 * @since 2017
 */
@RunWith(MockitoJUnitRunner.class)
public class AddProductToCatalogTest {

	@Mock
	private SQLDatabaseConnection sqlDatabaseConnection;

	@Before
	public void setup() {
		PropertyConfigurator.configure("../log4j.properties");
	}
	
	@Test
	public void addCatalogProductSuccessfulTest() {
		int senderID = 1;
		CatalogProduct catalogProduct = new CatalogProduct(0, "Shoko", null, null, null, 1.5, null, null);
		String command = new CommandWrapper(senderID, CommandDescriptor.ADD_PRODUCT_TO_CATALOG,
				new Gson().toJson(catalogProduct, CatalogProduct.class)).serialize();
		CommandExecuter commandExecuter = new CommandExecuter(command);
		CommandWrapper out;
		
		try {
			Mockito.doNothing().when(sqlDatabaseConnection).addProductToCatalog(senderID, catalogProduct);
		} catch (ProductAlreadyExistInCatalog | IngredientNotExist | ManufacturerNotExist | CriticalError | ClientNotConnected e) {
			fail();
		}
		
		out = commandExecuter.execute(sqlDatabaseConnection);
		
		assertEquals(ResultDescriptor.SM_OK, out.getResultDescriptor());
	}
	
	@Test
	public void addCatalogProductInvalidParamTest() {
		int senderID = 1;
		CatalogProduct catalogProduct = new CatalogProduct(0, "Shoko", null, null, null, -1, null, null);
		String command = new CommandWrapper(senderID, CommandDescriptor.ADD_PRODUCT_TO_CATALOG,
				new Gson().toJson(catalogProduct, CatalogProduct.class)).serialize();
		CommandExecuter commandExecuter = new CommandExecuter(command);
		CommandWrapper out;
		
		try {
			Mockito.doNothing().when(sqlDatabaseConnection).addProductToCatalog(senderID, catalogProduct);
		} catch (ProductAlreadyExistInCatalog | IngredientNotExist | ManufacturerNotExist | CriticalError | ClientNotConnected e) {
			fail();
		}
		
		out = commandExecuter.execute(sqlDatabaseConnection);
		
		assertEquals(ResultDescriptor.SM_INVALID_PARAMETER, out.getResultDescriptor());
	}
	
	@Test
	public void addCatalogProductIllegalCatalogProductTest() {
		assertEquals(ResultDescriptor.SM_ERR,
				(new CommandExecuter(new CommandWrapper(1, CommandDescriptor.ADD_PRODUCT_TO_CATALOG,
						new Gson().toJson("", String.class)).serialize())).execute(sqlDatabaseConnection)
								.getResultDescriptor());
	}
	
	@Test
	public void addCatalogProductProductAlreadyExistInCatalogTest() {
		int senderID = 1;
		CatalogProduct catalogProduct = new CatalogProduct(0, "Shoko", null, null, null, 4, null, null);
		String command = new CommandWrapper(senderID, CommandDescriptor.ADD_PRODUCT_TO_CATALOG,
				new Gson().toJson(catalogProduct, CatalogProduct.class)).serialize();
		CommandExecuter commandExecuter = new CommandExecuter(command);
		CommandWrapper out;
		
		try {
			Mockito.doThrow(new ProductAlreadyExistInCatalog()).when(sqlDatabaseConnection).addProductToCatalog(senderID, catalogProduct);
		} catch (IngredientNotExist | ManufacturerNotExist | CriticalError | ClientNotConnected e) {
			fail();
		} catch (ProductAlreadyExistInCatalog __) {
			/* Success */
		}
		
		out = commandExecuter.execute(sqlDatabaseConnection);
		
		assertEquals(ResultDescriptor.SM_CATALOG_PRODUCT_ALREADY_EXISTS, out.getResultDescriptor());
	}
	
	@Test
	public void addCatalogProductIngredientNotExistTest() {
		int senderID = 1;
		CatalogProduct catalogProduct = new CatalogProduct(0, "Shoko", null, null, null, 4, null, null);
		String command = new CommandWrapper(senderID, CommandDescriptor.ADD_PRODUCT_TO_CATALOG,
				new Gson().toJson(catalogProduct, CatalogProduct.class)).serialize();
		CommandExecuter commandExecuter = new CommandExecuter(command);
		CommandWrapper out;
		
		try {
			Mockito.doThrow(new IngredientNotExist()).when(sqlDatabaseConnection).addProductToCatalog(senderID, catalogProduct);
		} catch (ProductAlreadyExistInCatalog | ManufacturerNotExist | CriticalError | ClientNotConnected e) {
			fail();
		} catch (IngredientNotExist __) {
			/* Success */
		}
		
		out = commandExecuter.execute(sqlDatabaseConnection);
		
		assertEquals(ResultDescriptor.SM_INVALID_PARAMETER, out.getResultDescriptor());
	}
	
	@Test
	public void addCatalogProductManufacturerNotExistTest() {
		int senderID = 1;
		CatalogProduct catalogProduct = new CatalogProduct(0, "Shoko", null, null, null, 4, null, null);
		String command = new CommandWrapper(senderID, CommandDescriptor.ADD_PRODUCT_TO_CATALOG,
				new Gson().toJson(catalogProduct, CatalogProduct.class)).serialize();
		CommandExecuter commandExecuter = new CommandExecuter(command);
		CommandWrapper out;
		
		try {
			Mockito.doThrow(new ManufacturerNotExist()).when(sqlDatabaseConnection).addProductToCatalog(senderID, catalogProduct);
		} catch (ProductAlreadyExistInCatalog | IngredientNotExist | CriticalError | ClientNotConnected e) {
			fail();
		} catch (ManufacturerNotExist __) {
			/* Success */
		}
		
		out = commandExecuter.execute(sqlDatabaseConnection);
		
		assertEquals(ResultDescriptor.SM_INVALID_PARAMETER, out.getResultDescriptor());
	}
	
	@Test
	public void addCatalogProductCriticalErrorTest() {
		int senderID = 1;
		CatalogProduct catalogProduct = new CatalogProduct(0, "Shoko", null, null, null, 4, null, null);
		String command = new CommandWrapper(senderID, CommandDescriptor.ADD_PRODUCT_TO_CATALOG,
				new Gson().toJson(catalogProduct, CatalogProduct.class)).serialize();
		CommandExecuter commandExecuter = new CommandExecuter(command);
		CommandWrapper out;
		
		try {
			Mockito.doThrow(new CriticalError()).when(sqlDatabaseConnection).addProductToCatalog(senderID, catalogProduct);
		} catch (ProductAlreadyExistInCatalog | IngredientNotExist | ManufacturerNotExist | ClientNotConnected e) {
			fail();
		} catch (CriticalError __) {
			/* Success */
		}
		
		out = commandExecuter.execute(sqlDatabaseConnection);
		
		assertEquals(ResultDescriptor.SM_ERR, out.getResultDescriptor());
	}
	
	@Test
	public void addCatalogProductClientNotConnectedTest() {
		int senderID = 1;
		CatalogProduct catalogProduct = new CatalogProduct(0, "Shoko", null, null, null, 4, null, null);
		String command = new CommandWrapper(senderID, CommandDescriptor.ADD_PRODUCT_TO_CATALOG,
				new Gson().toJson(catalogProduct, CatalogProduct.class)).serialize();
		CommandExecuter commandExecuter = new CommandExecuter(command);
		CommandWrapper out;
		
		try {
			Mockito.doThrow(new ClientNotConnected()).when(sqlDatabaseConnection).addProductToCatalog(senderID, catalogProduct);
		} catch (ProductAlreadyExistInCatalog | IngredientNotExist | ManufacturerNotExist | CriticalError e) {
			fail();
		} catch (ClientNotConnected __) {
			/* Success */
		}
		
		out = commandExecuter.execute(sqlDatabaseConnection);
		
		assertEquals(ResultDescriptor.SM_SENDER_IS_NOT_CONNECTED, out.getResultDescriptor());
	}
}