package sig.plugin.TwosideKeeper.HelperStructures;

public enum SessionState {
	CREATE, //Creating a shop. Asks for amount to put in.
	PRICE, //Creating a shop. Asks for price of each unit.
	EDIT, //Editing a shop. Asks for amount to put in or take out.
	UPDATE, //Editing a shop. Asks for new price of each unit.
	PURCHASE,
	BUY_CREATE, //Creating a buy shop. Asks for amount you want to purchase.
	BUY_PRICE, //Creating a buy shop. Asks for buy price of each unit.
	BUY_EDIT, //Editing a shop. Asks for amount to withdraw.
	BUY_UPDATE, //Editing a shop. Asks for new price of each unit.
	SELL,
	SIGN_CHECK,
	CASH_CHECK,
	WITHDRAW,
	DEPOSIT,
	CONVERT
}
