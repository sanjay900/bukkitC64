

import java.awt.event.KeyEvent;

import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;

public class CommandHandler implements CommandExecutor{
	JaC64 jb;
	public CommandHandler(JaC64 jb) {
		this.jb = jb;
	}
	@Override
	public boolean onCommand(final CommandSender sender, Command command, String label, final String[] args) {
		if (args.length == 0) {
			return false;
		}
		switch (args[0]) {
		case "loadrom":
			sender.sendMessage("Loading: "+args[1]);
			jb.readDisk(args[1]);
			break;
		case "key":
			sender.sendMessage("Sending "+strJoin(args,1," ")+" to emulator");
			for (final char s : strJoin(args,1," ").toCharArray()) {
				jb.cpu.enterText(String.valueOf(s));
			}
			return true;
			
		}
		return false;
		
		
	}
	public static String strJoin(String[] aArr, int start, String sSep) {
        StringBuilder sbStr = new StringBuilder();
        for (int i = start, il = aArr.length; i < il; i++) {
            if (i > start)
                sbStr.append(sSep);
            sbStr.append(aArr[i]);
        }
        return sbStr.toString();
    }
}
