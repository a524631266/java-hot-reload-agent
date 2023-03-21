package com.fengjx.reload.core.adapter;

import java.io.File;
import java.io.IOException;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.net.JarURLConnection;
import java.net.URL;
import java.net.URLConnection;
import java.util.Arrays;
import java.util.jar.JarFile;

public class JarURLConnectionAdapter {
    URLConnection urlConnection;
    String jarUri;
    String entryName;

    public JarURLConnectionAdapter(URL url) throws IOException {
        this.urlConnection = url.openConnection();
        this.jarUri = url.toExternalForm().substring(0, url.toExternalForm().lastIndexOf("!/"));
        this.entryName = url.toExternalForm().replace(this.jarUri, "");
    }

    /**
     * 是在文件后面的com/github/a524631266/owgeneratorcode/action
     *
     * @return
     */
    public String getEntryName() {
        try {
            Class<? extends URLConnection> aClass = urlConnection.getClass();
            if (Arrays.stream(aClass.getMethods()).anyMatch(a -> a.getName().equals("getEntryName"))) {
                Method entryNameMethod = aClass.getMethod("getEntryName");
                entryNameMethod.setAccessible(true);
                String rootEntryName = (String) entryNameMethod.invoke(urlConnection);
                return rootEntryName;
            } else {
                return entryName;
            }
        } catch (NoSuchMethodException | IllegalAccessException | InvocationTargetException e) {
            throw new RuntimeException("getEntryName error", e);
        }
    }

    public JarFile getJarFile() {
        try {
            Class<? extends URLConnection> aClass = urlConnection.getClass();
            if(Arrays.stream(aClass.getMethods()).anyMatch(a -> a.getName().equals("getJarFile"))){
                Method getJarFile = aClass.getMethod("getJarFile");
                getJarFile.setAccessible(true);
                JarFile jarFile = (JarFile) getJarFile.invoke(urlConnection);
                return jarFile;
            }
            return new JarFile(new File(jarUri.replace("jar:file:", "")));

        } catch (NoSuchMethodException | IllegalAccessException | InvocationTargetException | IOException e) {
            throw new RuntimeException("getJarFile error", e);
        }
    }
}
