package com.dreamfabric.jac64;

import java.io.File;
import java.io.InputStream;
import java.net.URL;
import java.nio.file.Path;
import java.nio.file.Paths;

import org.bukkit.Bukkit;

/**
 * Describe class SELoader here.
 *
 *
 * Created: Mon Oct 16 21:17:54 2006
 *
 * @author <a href="mailto:Joakim@BOTBOX"></a>
 * @version 1.0
 */

public class SELoader extends Loader {

  String codebase = null;

  public SELoader() {
  }

  public SELoader(String codebase) {
    this.codebase = codebase;
  }

  public InputStream getResourceStream(String resource) {
    try {
    	URL url;
        File f = new File(Bukkit.class.getProtectionDomain().getCodeSource().getLocation().toURI()).getParentFile().getParentFile();
        Path path = Paths.get(f.getPath()+"/c64/boot/"+resource);
        url = path.toUri().toURL();
      return url.openConnection().getInputStream();
    } catch (Exception e) {
      e.printStackTrace();
    }
    return null;
  }
}
