package sig.plugin.TwosideKeeper.HelperStructures;

public enum SessionState {
	CREATE, //Creating a shop. Asks for amount to put in.
	PRICE, //Creating a shop. Asks for price of each unit.
	EDIT, //Editing a shop. Asks for amount to put in or take out.
	UPDATE, //Editing a shop. Asks for new price of each unit.
	PURCHASE
}
