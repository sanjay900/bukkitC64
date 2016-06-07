import java.awt.image.BufferedImage;

import org.bukkit.ChatColor;
import com.gmail.filoghost.holographicdisplays.disk.Configuration;

public class ImageMessage {
	private SplitLine lines;

	public ImageMessage(BufferedImage image) {
		lines = toImgMessage(image);
	}

	private SplitLine toImgMessage(BufferedImage image) {

		String[] lines1 = new String[image.getHeight()];
		String[] lines2 = new String[image.getHeight()];
		String[] lines3 = new String[image.getHeight()];
		String imageSymbol = Configuration.imageSymbol;
		for (int y = 0; y < image.getHeight(); y++) {
			StringBuffer line1 = new StringBuffer();
			StringBuffer line2 = new StringBuffer();
			StringBuffer line3 = new StringBuffer();
			ChatColor previous = ChatColor.RESET;
			for (int x = 0; x < image.getWidth(); x++) {
				int rgb = image.getRGB(x, y);
				ChatColor currentColor;
				switch (rgb) {
				case 0xff000000: // 0 Black
					currentColor = ChatColor.BLACK;
					break;
				case 0xffffffff: // 1 White
					currentColor = ChatColor.WHITE;
					break;
				case 0xffe04040: // 2 Red
					currentColor = ChatColor.DARK_RED;
					break;
				case 0xff60ffff: // 3 Cyan
					currentColor = ChatColor.DARK_AQUA;
					break;
				case 0xffe060e0: // 4 Purple
					currentColor = ChatColor.DARK_PURPLE;
					break;
				case 0xff40e040: // 5 Green
					currentColor = ChatColor.DARK_GREEN;
					break;
				case 0xff4040e0: // 6 Blue
					currentColor = ChatColor.DARK_BLUE;
					break;
				case 0xffffff40: // 7 Yellow
					currentColor = ChatColor.YELLOW;
					break;
				case 0xffe0a040: // 8 Orange
					currentColor = ChatColor.GOLD;
					break;
				case 0xff9c7448: // 9 Brown
					currentColor = ChatColor.BLACK;
					break;
				case 0xffffa0a0: // 10 Lt.Red
					currentColor = ChatColor.RED;
					break;
				case 0xff545454: // 11 Dk.Gray
					currentColor = ChatColor.DARK_GRAY;
					break;
				case 0xff888888: // 12 Gray
					currentColor = null;
					break;
				case 0xffa0ffa0: // 13 Lt.Green
					currentColor = ChatColor.GREEN;
					break;
				case 0xffa0a0ff: // 14 Lt.Blue
					currentColor = ChatColor.BLUE;
					break;
				case 0xffc0c0c0: // 15 Lt.Gray
					currentColor = ChatColor.GRAY;
					break;
				default:
					currentColor = ChatColor.BLUE;
				}
				if (currentColor != null) {
					if (previous != currentColor) {
						if (x > (image.getWidth()/3)*2) {
							line3.append(currentColor.toString());
						} else if (x > image.getWidth()/3) {
							line2.append(currentColor.toString());
						} else {
							line1.append(currentColor.toString());
						}
						previous = currentColor;
					} 

					if (x == 0) {
						line1.append(currentColor.toString());
					}
					if (x == image.getWidth()/3) {
						line2.append(currentColor.toString());
					}
					if (x == (image.getWidth()/3)*2) {
						line3.append(currentColor.toString());
					}

				}
				if (x > (image.getWidth()/3)*2) {
					line3.append(currentColor == null?" ":imageSymbol);
				} else if (x > image.getWidth()/3) {
					line2.append(currentColor == null?" ":imageSymbol);
				} else {
					line1.append(currentColor == null?" ":imageSymbol);
				}


			}
			lines1[y] = line1.toString();
			lines2[y] = line2.toString();
			lines3[y] = line3.toString();
		}
		return new SplitLine(lines1,lines2,lines3);
	}

	public SplitLine getLines() {
		return lines;
	}
	public class SplitLine {
		public String[] lines1;
		public String[] lines2;
		public String[] lines3;
		public SplitLine(String[] lines1,String[] lines2,String[] lines3) {
			this.lines1 = lines1;
			this.lines2 = lines2;
			this.lines3 = lines3;
		}
	}
}


