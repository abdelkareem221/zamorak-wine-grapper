package miningg;


import org.dreambot.api.methods.Calculations;
import org.dreambot.api.methods.item.GroundItems;
import org.dreambot.api.methods.magic.Magic;
import org.dreambot.api.methods.magic.Normal;
import org.dreambot.api.methods.map.Area;
import org.dreambot.api.script.AbstractScript;
import org.dreambot.api.script.ScriptManifest;
import org.dreambot.api.script.Category;
import org.dreambot.api.methods.container.impl.Inventory;
import org.dreambot.api.methods.container.impl.bank.BankLocation;
import org.dreambot.api.wrappers.interactive.GameObject;
import org.dreambot.api.wrappers.items.GroundItem;




@ScriptManifest(author = "abood221", name = "Wine of zamorak", version = 1.0, description = "wine grapper", category = Category.MONEYMAKING)
public class moooon extends AbstractScript {
private Area WINESS = new Area(2936, 3519, 2941, 3512);	
private Area WINES1 = new Area(2939, 3516, 2939, 3516);
@SuppressWarnings("unused")
private Area faladorBank = new Area(3009, 3358, 3021, 3355);
public void onStart() {log("welcometo WINEGRAPPER by abood221.");
log("start at WEST falador bank WITH LAW RUNES IN INVY WATER RUNES IF U WANT TO USE FALADOR TELE AND WIELD AIR STAF AND ZAMORAK ROBES");
log("if u experience any issue plz report them to me in forum");
log("enjoy");

}
private enum State {
	STEAL, CLIMPDOWN, WAIT,walktobank,comeback
};

@SuppressWarnings("deprecation")
private State getState() {
	 getGroundItems();
	GroundItem wine = GroundItems.closest(c -> c != null && c.getName().equals("Wine of zamorak"));
	getInventory();
	if (Inventory.isFull() && getLocalPlayer().getTile().getZ()==1)
		return State.CLIMPDOWN;
	if (!Inventory.isFull() && getLocalPlayer().getTile().getZ()==0)
		return State.comeback;
	if (Inventory.isFull() && getLocalPlayer().getTile().getZ()==0)
		return State.walktobank;
	if (wine != null && getLocalPlayer().getTile().getZ()==1 )
		return State.STEAL;
	return State.WAIT;
}



@SuppressWarnings({ "deprecation", "static-access" })
@Override
public int onLoop() {
	switch (getState()) {
	case STEAL:
		GroundItem wine = GroundItems.closest(c -> c != null && c.getName().equals("Wine of zamorak"));
		if (wine != null) {
			getMagic();
			Magic.castSpellOn(Normal.TELEKINETIC_GRAB, wine);
			sleep(1500);
		}
		
		break;
	case CLIMPDOWN:
		Magic.castSpell(Normal.FALADOR_TELEPORT);
		GameObject ladder = getGameObjects().closest("Ladder");
		if (ladder != null) {
			ladder.interact("climb");
			sleep(3000);
		}
		break;
	case walktobank:

        if (getInventory().isFull()) {
            //Is the bank open?
            if (getBank().isOpen()) {
                //If the bank is open deposit all cowhides
                getBank().depositAll("Wine of zamorak");
                //Waits a bit
                sleep(250);
                //Closes bank and continues
                getBank().close();
            }  else {
                //If the bank is not open, go open it
                getBank().open(BankLocation.FALADOR_WEST);
            }
        }
		
		break;
	case comeback:
		getWalking().walk(WINESS.getRandomTile());
		GameObject ladder1 = getGameObjects().closest("Ladder");
		GameObject torch = getGameObjects().closest("Standing torch");
		if (ladder1 != null && torch != null) {
			ladder1.interact("climb");
			sleep(1500);
			
		}
			break;
	case WAIT:
		sleep(300);
		break;
	}
	return Calculations.random(500, 600);
}
}

