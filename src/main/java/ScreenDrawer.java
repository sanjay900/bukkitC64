import java.awt.image.BufferedImage;

import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.entity.ArmorStand;
import org.bukkit.entity.EntityType;
import org.bukkit.plugin.Plugin;

import com.gmail.filoghost.holographicdisplays.api.Hologram;
import com.gmail.filoghost.holographicdisplays.api.HologramsAPI;
import com.gmail.filoghost.holographicdisplays.api.line.TextLine;

import net.md_5.bungee.api.ChatColor;

public class ScreenDrawer implements Runnable{
	public volatile boolean shutdown = false;
	JaC64 jb;
	Hologram hologram1;
	Hologram hologram2;
	Hologram hologram3;
	public ScreenDrawer(JaC64 javaboy) {
		this.jb = javaboy;
		Location l = new Location(Bukkit.getWorld("gba"), 110, 150.0, 30.0);
		hologram1 = HologramsAPI.createHologram((Plugin)this.jb, l.clone().subtract(30.6+30.3, 0, 0));
		hologram2 = HologramsAPI.createHologram((Plugin)this.jb, l.clone().subtract(30.3, 0, 0));
		hologram3 = HologramsAPI.createHologram((Plugin)this.jb, l);
		for (int i = 0; i< 284; i++)  {
			hologram1.appendTextLine("");
			hologram2.appendTextLine("");
			hologram3.appendTextLine("");
		}
	}
	@Override
	public void run() {
		if (!shutdown && jb.cpu.running) {

				try {
				ImageMessage msg = new ImageMessage((BufferedImage)jb.scr.screen);
				for (int i = 0; i < msg.getLines().lines1.length; ++i) {
				TextLine t = (TextLine) this.hologram1.getLine(i);
				t.setText(msg.getLines().lines1[i]);
				t = (TextLine) this.hologram2.getLine(i);
				t.setText(msg.getLines().lines2[i]);
				t = (TextLine) this.hologram3.getLine(i);
				t.setText(msg.getLines().lines3[i]);
				}
				} catch (Exception ex) {
					ex.printStackTrace();
				}
				
			}

			
		}




	

	public void shutdown() {
		this.hologram1.delete();
		this.hologram2.delete();
		this.hologram3.delete();
	}

}
