/**
 * encoding: UTF-8
 * JaC64 - Application for JaC64 emulator
 * A Swing UI for the JaC64 emulator for download to Java enabled
 * Desktop computers.
 * Created: Sat Dec 08 23:27:15 2007
 *
 * @author Joakim Eriksson, Dreamfabric / joakime@sics.se
 * @version 1.0
 */
import java.awt.event.KeyEvent;

import org.bukkit.Bukkit;
import org.bukkit.plugin.java.JavaPlugin;

import com.dreamfabric.c64utils.Debugger;
import com.dreamfabric.jac64.C64Reader;
import com.dreamfabric.jac64.C64Screen;
import com.dreamfabric.jac64.CPU;
import com.dreamfabric.jac64.SELoader;
import com.dreamfabric.jac64.SIDMixer;

public class JaC64 extends JavaPlugin{

  private static final String ABOUT_MESSAGE = 
    "JaC64 version: " + C64Screen.version + "\n" +
    "JaC64 is a Java-based C64 emulator by Joakim Eriksson\n" +
    "The SID emulation use the resid Java port by Ken Händel\n\n" +
    "For more information see: http://www.jac64.com/";

  private C64Reader reader;
  public C64Screen scr;
  private boolean fullscreen = false;
  public ScreenDrawer screenDrawer;
  public CPU cpu;
  @Override
  public void onDisable() {
	  screenDrawer.shutdown();
	  //84,113,64 = Holo location
  }
  @Override
  public void onEnable() {
    SIDMixer.DL_BUFFER_SIZE = 16384;
    Debugger monitor = new Debugger();

    cpu = new CPU(monitor, "", new SELoader());
    scr = new C64Screen(monitor, true);
    cpu.init(scr);

    // Reader available after init!
    scr.init(cpu);

    scr.registerHotKey(KeyEvent.VK_BACK_SPACE, KeyEvent.CTRL_DOWN_MASK |
		       KeyEvent.ALT_DOWN_MASK
		       , "reset()", cpu);

    scr.registerHotKey(KeyEvent.VK_F12, KeyEvent.CTRL_DOWN_MASK
		       , "toggleFullScreen()", this);

    reader = new C64Reader(); // scr.getDiskDrive().getReader();
    reader.setCPU(cpu);
    cpu.getDrive().setReader(reader);
    Bukkit.getScheduler().runTaskAsynchronously(this, new Runnable(){

		@Override
		public void run() {
		    cpu.start();
		}});
    screenDrawer = new ScreenDrawer(this);
    Bukkit.getScheduler().runTaskTimerAsynchronously(this, screenDrawer,1l,1l);
    getCommand("c64").setExecutor(new CommandHandler(this));
    //KeyboardFocusManager.
     // getCurrentKeyboardFocusManager().addKeyEventDispatcher(this);
  }


/*
  public void actionPerformed(ActionEvent ae) {
    String cmd = ae.getActionCommand();
    if ("Open File/Disk".equals(cmd)) {
      if (fileTable == null) {
        // Show the table somewhere!!!
        fileTable = new JTable(dataModel);
        fileTable.getColumnModel().getColumn(1).setMaxWidth(50);
        fileTable.getColumnModel().getColumn(2).setMaxWidth(50);
        fileTable.setShowGrid(false);
        fileTable.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
      }
      readDisk();
    } else if ("Reset".equals(cmd)){
      cpu.reset();
    } else if ("Hard Reset".equals(cmd)){
      cpu.hardReset();
    } else if ("About JaC64".equals(cmd)) {
      showAbout();
    } else if ("Load File".equals(cmd)) {
      if (loadFile == null) {
        loadFile = new JDialog(C64Win, "Load file from disk");
        loadFile.setAlwaysOnTop(true);
        loadFile.setVisible(true);
        loadFile.setLayout(new BorderLayout());
        loadFile.add(new JScrollPane(fileTable), BorderLayout.CENTER);
        loadFile.add(fileTable.getTableHeader(), BorderLayout.NORTH);
        JPanel jp = new JPanel();
        JButton jb;
        jp.add(jb = new JButton("Load file"));
        jb.addActionListener(new ActionListener() {
          public void actionPerformed(ActionEvent ae) {
            reader.readFile(dirEntries[fileTable.getSelectedRow()].name);
            loadFile.setVisible(false);
          }
        });
        jp.add(jb = new JButton("Cancel"));
        jb.addActionListener(new ActionListener() {
          public void actionPerformed(ActionEvent ae) {
            loadFile.setVisible(false);
          }
        });    
        loadFile.add(jp, BorderLayout.SOUTH);
        loadFile.setSize(300, 400);
      }
      loadFile.setVisible(true);
    } else if (cmd.startsWith("Color Set")) {
      int cs = cmd.charAt(10) - '1';
      System.out.println("Color set: " + cs);
      scr.setColorSet(cs);
    } else if (cmd.equals(SID_TYPES[0])) {
      scr.setSID(C64Screen.RESID_6581);
    } else if (cmd.equals(SID_TYPES[1])) {
        scr.setSID(C64Screen.RESID_8580);
    } else if (cmd.equals(SID_TYPES[2])) {
        scr.setSID(C64Screen.JACSID);
    } else if (cmd.equals(JOYSTICK[0])) {
      scr.setStick(true);
    } else if (cmd.equals(JOYSTICK[1])) {
      scr.setStick(false);
    }
  }
*/
  
  public void toggleFullScreen() {
    System.out.println("Toggle fullscreen called!");
    setFull(!fullscreen);
  }
/*
  private void readDisk() {
    if (fileDialog == null)
      fileDialog = new FileDialog(C64Win, "Select File/Disk to Load");
    fileDialog.setVisible(true);

    String name = fileDialog.getDirectory() + fileDialog.getFile();
    if (!readDisk(name)) {
      dirEntries = (DirEntry[]) reader.getDirNames().toArray(new DirEntry[0]);
      fileTable.tableChanged(new TableModelEvent(dataModel));
    }
  }
*/
  public boolean readDisk(String name) {
    System.out.println("READING FROM: " + name);
    if ((name.toLowerCase()).endsWith(".d64"))
      reader.readDiskFromFile(name);
    else if ((name.toLowerCase()).endsWith(".t64")||(name.toLowerCase()).endsWith(".tap"))
      reader.readTapeFromFile(name);
    else if ((name.toLowerCase()).endsWith(".sid"))
          reader.readSIDFromFile(name);
    else if (name.toLowerCase().endsWith(".prg") ||
        name.toLowerCase().endsWith(".p00")) {
      cpu.reset();
      try {
        Thread.sleep(10);
      }catch (Exception e2) {
        System.out.println("Exception while sleeping...");
      }
      while(!scr.ready()) {
        try {
          Thread.sleep(100);
        }catch (Exception e2) {
          System.out.println("Exception while sleeping...");
        }
      }
      reader.readPGM(name, -1);
      cpu.runBasic();
      return true;
    }
    return false;
  }

  private void setFull(boolean full) {
//     JWindow jw = full ? C64Scr : null;
//     java.awt.GraphicsEnvironment.getLocalGraphicsEnvironment().
//       getDefaultScreenDevice().setFullScreenWindow(jw);
//     if (!full) {
//       C64Scr.setSize(386 * 2, 284 * 2);
//       C64Scr.validate();
//     }
//     fullscreen = full;
  }
}