package SMExceptions;

import CommonDefs.GuiCommonDefs;
import GuiUtils.DialogMessagesService;


/**
 * CommonExceptions - here we define common exceptions 
 * @author Shimon Azulay
 * @since 21.5.17
 */
public class CommonExceptions extends SMException {
	
	private static final long serialVersionUID = -6485599202576614258L;

	/**
	 * Thrown when unexpected error occurred
	 *
	 */
	public static class CriticalError extends CommonExceptions {

		private static final long serialVersionUID = -1281316098307233162L;
		
		@Override
		public void showInfoToUser() {
			DialogMessagesService.showErrorDialog(GuiCommonDefs.criticalErrorTitle, GuiCommonDefs.unexpectedFailureMsg,
					GuiCommonDefs.criticalErrorMsg);
		}
	}

}