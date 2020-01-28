

package kr.com.illootech.framework.utils.reflect;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.Enumeration;
import java.io.IOException;
import java.util.jar.JarEntry;
import kr.com.illootech.framework.file.log.Logger;
import kr.com.illootech.framework.file.log.LoggerElements;
import java.util.HashMap;
import java.net.URLClassLoader;
import java.net.URL;
import java.util.jar.JarFile;

public class DynamicClassLoader
{
    private final String procname = "DCLASS.LDR";
    private String jarFileName;
    private JarFile jarFile;
    private URL url;
    private URLClassLoader loader;
    private HashMap<String, Class<?>> srcClassMap;
    private HashMap<String, Object> srcClassInstanceMap;
    
    public DynamicClassLoader() {
        this.jarFileName = null;
        this.jarFile = null;
        this.url = null;
        this.loader = null;
        this.srcClassMap = null;
        this.srcClassInstanceMap = null;
        this.srcClassMap = new HashMap<String, Class<?>>();
        this.srcClassInstanceMap = new HashMap<String, Object>();
    }
    
    public boolean loadFile(final String jarFile) {
        boolean result = false;
        try {
            if (jarFile == null || jarFile.trim().equals("")) {
                Logger.sysInfo(String.format("[%1$-20s][%2$-10s][Load remote.class.file. Arguments is not available. Jar.file '%3$s'] - FAIL", "DCLASS.LDR", "ERROR", (jarFile != null) ? jarFile : "N/A"), LoggerElements.LOG_LEVEL3);
                return result;
            }
            this.jarFileName = jarFile;
            this.url = new URL("file:" + jarFile);
            this.loader = new URLClassLoader(new URL[] { this.url });
            if (this.loader != null) {
                this.jarFile = new JarFile(this.jarFileName);
                result = true;
            }
            return result;
        }
        catch (Exception e) {
            Logger.error(e);
            return false;
        }
        finally {
            Logger.sysInfo(String.format("[%1$-20s][%2$-10s][Load file. '%3$s'] - %4$s", "DCLASS.LDR", result ? "RUNNING" : "ERROR", (jarFile != null) ? jarFile : "N/A", result ? "SUCCESS" : "FAIL"), LoggerElements.LOG_LEVEL3);
        }
    }
    
    public boolean loadClasses(final String className) {
        boolean result = false;
        JarFile jarFile = null;
        try {
            if (this.loader == null || this.jarFileName == null || this.jarFileName.trim().equals("") || this.jarFile == null) {
                Logger.sysInfo(String.format("[%1$-20s][%2$-10s][Load class. Arguments is not available.] - FAIL", "DCLASS.LDR", "ERROR"), LoggerElements.LOG_LEVEL3);
                return result;
            }
            final Enumeration<JarEntry> entries = this.jarFile.entries();
            while (entries.hasMoreElements()) {
                JarEntry entry = null;
                Class<?> tmpClass = null;
                Object tmpClassInstance = null;
                try {
                    entry = entries.nextElement();
                    if (entry == null) {
                        continue;
                    }
                    if (!entry.getName().endsWith(".class")) {
                        continue;
                    }
                    String tmpClassName = entry.getName();
                    if (tmpClassName == null) {
                        continue;
                    }
                    tmpClassName = tmpClassName.replace(".class", "").replace("/", ".");
                    if (!tmpClassName.equals(className)) {
                        continue;
                    }
                    try {
                        tmpClass = Class.forName(tmpClassName, true, this.loader);
                        if (tmpClass == null) {
                            continue;
                        }
                        if (!this.setClass(tmpClassName, tmpClass)) {
                            continue;
                        }
                        tmpClassInstance = tmpClass.newInstance();
                        Logger.sysInfo(String.format("[%1$-20s][%2$-10s][Load classes. Class.instance '%3$s' created new instance]", "DCLASS.LDR", "RUNNING", (tmpClass != null) ? tmpClass.getName() : "N/A"), LoggerElements.LOG_LEVEL3);
                    }
                    catch (NoClassDefFoundError ncdfe) {
                        Logger.sysInfo(String.format("[%1$-20s][%2$-10s][Load classes '%3$s'. No.Class.Def.Found Exception. '%4$s'] - SKIP", "DCLASS.LDR", "RUNNING", (tmpClassName != null) ? tmpClassName : "N/A", (ncdfe != null && ncdfe.getMessage() != null) ? ncdfe.getMessage() : "N/A"), LoggerElements.LOG_LEVEL3);
                    }
                    catch (InstantiationException ie) {
                        Logger.sysInfo(String.format("[%1$-20s][%2$-10s][Load classes '%3$s'. This class is abstract or interface '%4$s'] - SKIP", "DCLASS.LDR", "RUNNING", (tmpClassName != null) ? tmpClassName : "N/A", (ie != null && ie.getMessage() != null) ? ie.getMessage() : "N/A"), LoggerElements.LOG_LEVEL3);
                    }
                    finally {
                        if (!this.setClassInstance(tmpClassName, tmpClassInstance)) {
                            this.srcClassMap.remove(tmpClassName);
                        }
                    }
                    if (this.setClassInstance(tmpClassName, tmpClassInstance)) {
                        continue;
                    }
                    this.srcClassMap.remove(tmpClassName);
                }
                catch (Exception e) {
                    Logger.error(e);
                }
            }
            result = true;
            return result;
        }
        catch (Exception e2) {
            Logger.error(e2);
            return false;
        }
        finally {
            try {
                if (jarFile != null) {
                    jarFile.close();
                }
                jarFile = null;
            }
            catch (IOException ie2) {
                Logger.error(ie2);
            }
        }
    }
    
    public boolean loadClassesAll() {
        boolean result = false;
        try {
            if (this.loader == null || this.jarFileName == null || this.jarFileName.trim().equals("")) {
                Logger.sysInfo(String.format("[%1$-20s][%2$-10s][Load classes All. Arguments is not available.] - FAIL", "DCLASS.LDR", "ERROR"), LoggerElements.LOG_LEVEL3);
                return result;
            }
            final Enumeration<JarEntry> entries = this.jarFile.entries();
            if (entries == null) {
                Logger.sysInfo(String.format("[%1$-20s][%2$-10s][Load classes All. File '%3$s' entries is not available] - FAIL", "DCLASS.LDR", "ERROR", (this.jarFileName != null) ? this.jarFileName : "N/A"), LoggerElements.LOG_LEVEL3);
                return result;
            }
            while (entries.hasMoreElements()) {
                JarEntry entry = null;
                Class<?> tmpClass = null;
                Object tmpClassInstance = null;
                try {
                    entry = entries.nextElement();
                    if (entry == null) {
                        Logger.sysInfo(String.format("[%1$-20s][%2$-10s][Load classes All. File '%3$s' File '%3$s' entry has not next element] - FAIL", "DCLASS.LDR", "ERROR", (this.jarFileName != null) ? this.jarFileName : "N/A"), LoggerElements.LOG_LEVEL3);
                    }
                    else {
                        if (!entry.getName().endsWith(".class")) {
                            continue;
                        }
                        String className = entry.getName();
                        if (className == null) {
                            Logger.sysInfo(String.format("[%1$-20s][%2$-10s][Load classes All. Class.name do not exist in the entries] - FAIL", "DCLASS.LDR", "ERROR"), LoggerElements.LOG_LEVEL3);
                        }
                        else {
                            className = className.replace(".class", "").replace("/", ".");
                            try {
                                tmpClass = Class.forName(className, true, this.loader);
                                if (tmpClass == null) {
                                    continue;
                                }
                                if (!this.setClass(className, tmpClass)) {
                                    continue;
                                }
                                tmpClassInstance = tmpClass.newInstance();
                                Logger.sysInfo(String.format("[%1$-20s][%2$-10s][Load classes All. Class.instance '%3$s' created new instance]", "DCLASS.LDR", "RUNNING", (tmpClass != null) ? tmpClass.getName() : "N/A"), LoggerElements.LOG_LEVEL3);
                            }
                            catch (NoClassDefFoundError ncdfe) {
                                Logger.sysInfo(String.format("[%1$-20s][%2$-10s][Load classes All. No.Class.Def.Found Exception. '%3$s'] - SKIP", "DCLASS.LDR", "RUNNING", (ncdfe != null && ncdfe.getMessage() != null) ? ncdfe.getMessage() : "N/A"), LoggerElements.LOG_LEVEL3);
                            }
                            catch (InstantiationException ie) {
                                Logger.sysInfo(String.format("[%1$-20s][%2$-10s][Load classes All. This class is abstract or interface '%3$s'] - SKIP", "DCLASS.LDR", "RUNNING", (ie != null && ie.getMessage() != null) ? ie.getMessage() : "N/A"), LoggerElements.LOG_LEVEL3);
                            }
                            finally {
                                if (!this.setClassInstance(className, tmpClassInstance)) {
                                    this.srcClassMap.remove(className);
                                }
                            }
                            if (this.setClassInstance(className, tmpClassInstance)) {
                                continue;
                            }
                            this.srcClassMap.remove(className);
                        }
                    }
                }
                catch (Exception e) {
                    Logger.error(e);
                }
            }
            result = true;
            return result;
        }
        catch (Exception e2) {
            Logger.error(e2);
            return false;
        }
        finally {
            try {
                if (this.jarFile != null) {
                    this.jarFile.close();
                }
                this.jarFile = null;
            }
            catch (IOException ie2) {
                Logger.error(ie2);
            }
        }
    }
    
    public boolean setClass(final String classname, final Class<?> classObject) {
        boolean result = false;
        try {
            if (this.srcClassMap == null || classObject == null) {
                Logger.sysInfo(String.format("[%1$-20s][%2$-10s][Set class. Class.name '%3$s' or Object is not available] - FAIL", "DCLASS.LDR", "ERROR"), LoggerElements.LOG_LEVEL3);
                return result;
            }
            this.srcClassMap.put(classname, classObject);
            if (this.srcClassMap.containsKey(classname)) {
                result = true;
            }
            return result;
        }
        catch (Exception e) {
            Logger.error(e);
            return false;
        }
        finally {
            Logger.sysInfo(String.format("[%1$-20s][%2$-10s][Set class. Class.name '%3$s'] - %4$s", "DCLASS.LDR", result ? "RUNNING" : "ERROR", (classname != null) ? classname : "N/A", result ? "SUCCESS" : "FAIL"), LoggerElements.LOG_LEVEL3);
        }
    }
    
    public Class<?> getClass(final String className) {
        Class<?> result = null;
        try {
            if (className == null) {
                Logger.sysInfo(String.format("[%1$-20s][%2$-10s][Get class. Class.name '%3$s' or Object is not available] - FAIL", "DCLASS.LDR", "ERROR"), LoggerElements.LOG_LEVEL3);
                return result;
            }
            result = this.srcClassMap.get(className);
            return result;
        }
        catch (Exception e) {
            Logger.error(e);
            return null;
        }
        finally {
            Logger.sysInfo(String.format("[%1$-20s][%2$-10s][Get class. Class.name '%3$s'] - %4$s", "DCLASS.LDR", (result != null) ? "RUNNING" : "ERROR", (className != null) ? className : "N/A", (result != null) ? "SUCCESS" : "FAIL"), LoggerElements.LOG_LEVEL3);
        }
    }
    
    public boolean setClassInstance(final String classname, final Object classInstance) {
        boolean result = false;
        try {
            if (this.srcClassInstanceMap == null || classInstance == null || classname == null) {
                Logger.sysInfo(String.format("[%1$-20s][%2$-10s][Set class instance. Class.name '%3$s'] - %4$s", "DCLASS.LDR", result ? "RUNNING" : "ERROR", (classname != null) ? classname : "N/A", result ? "SUCCESS" : "FAIL"), LoggerElements.LOG_LEVEL3);
                return result;
            }
            this.srcClassInstanceMap.put(classname, classInstance);
            if (this.srcClassInstanceMap.containsKey(classname)) {
                result = true;
            }
            return result;
        }
        catch (Exception e) {
            Logger.error(e);
            return false;
        }
        finally {
            Logger.sysInfo(String.format("[%1$-20s][%2$-10s][Set class instance. Class.name '%3$s'] - %4$s", "DCLASS.LDR", result ? "RUNNING" : "ERROR", (classname != null) ? classname : "N/A", result ? "SUCCESS" : "FAIL"), LoggerElements.LOG_LEVEL3);
        }
    }
    
    public Object getClassInstance(final String className) {
        Object result = null;
        try {
            if (className == null) {
                Logger.sysInfo(String.format("[%1$-20s][%2$-10s][Get class instance. Class.name '%3$s' or Object is not available] - FAIL", "DCLASS.LDR", "ERROR"), LoggerElements.LOG_LEVEL3);
                return result;
            }
            result = this.srcClassInstanceMap.get(className);
            return result;
        }
        catch (Exception e) {
            Logger.error(e);
            return null;
        }
        finally {
            Logger.sysInfo(String.format("[%1$-20s][%2$-10s][Get class instance. Class.name '%3$s'] - %4$s", "DCLASS.LDR", (result != null) ? "RUNNING" : "ERROR", (className != null) ? className : "N/A", (result != null) ? "SUCCESS" : "FAIL"), LoggerElements.LOG_LEVEL3);
        }
    }
    
    public Method[] getClassMethods(final Class<?> srcClass) {
        Method[] result = null;
        try {
            if (srcClass == null) {
                Logger.sysInfo(String.format("[%1$-20s][%2$-10s][Get class instance. Class.name '%3$s' or Object is not available] - FAIL", "DCLASS.LDR", "ERROR"), LoggerElements.LOG_LEVEL3);
                return result;
            }
            result = srcClass.getMethods();
            return result;
        }
        catch (Exception e) {
            Logger.error(e);
            return null;
        }
        finally {
            Logger.sysInfo(String.format("[%1$-20s][%2$-10s][Get class's methods. Class.name '%3$s'] - %4$s", "DCLASS.LDR", (result != null) ? "RUNNING" : "ERROR", (srcClass != null && srcClass.getName() != null) ? srcClass.getName() : "N/A", (result != null) ? "SUCCESS" : "FAIL"), LoggerElements.LOG_LEVEL3);
        }
    }
    
    public void release() {
        try {
            if (this.srcClassInstanceMap != null) {
                this.srcClassInstanceMap.clear();
            }
            if (this.srcClassMap != null) {
                this.srcClassMap.clear();
            }
            if (this.loader != null) {
                this.loader.close();
            }
            if (this.url != null) {
                this.url = null;
            }
            if (this.jarFile != null) {
                this.jarFile.close();
            }
        }
        catch (Exception e) {
            Logger.error(e);
        }
    }
    
    public Object execute(final String classname, final String methodName, final Object... args) {
        Object result = null;
        Class<?> tmpClass = null;
        Object tmpClassInstance = null;
        Method[] methods = null;
        Class<?> returnType = null;
        try {
            if (this.srcClassMap == null || this.srcClassInstanceMap == null || classname == null || methodName == null) {
                Logger.sysInfo(String.format("[%1$-20s][%2$-10s][execute. Argument is not available. Class '%3$s'. Method '%4$s'] - FAIL", "DCLASS.LDR", "ERROR", (classname != null) ? classname : "N/A", (methodName != null) ? methodName : "N/A", "FAIL"), LoggerElements.LOG_LEVEL3);
                return result;
            }
            tmpClass = this.getClass(classname);
            if (tmpClass == null) {
                return result;
            }
            methods = this.getClassMethods(tmpClass);
            if (methods == null) {
                return result;
            }
            tmpClassInstance = this.getClassInstance(classname);
            if (tmpClassInstance == null) {
                return result;
            }
            Method[] array;
            for (int length = (array = methods).length, i = 0; i < length; ++i) {
                final Method method = array[i];
                if (method.getName().equals(methodName)) {
                    final int tmpParmeterCount = method.getParameterTypes().length;
                    if (tmpParmeterCount == args.length) {
                        result = method.invoke(tmpClassInstance, args);
                        returnType = method.getReturnType();
                        break;
                    }
                }
            }
            return result;
        }
        catch (IllegalArgumentException e) {
            Logger.error(e);
            return null;
        }
        catch (IllegalAccessException e2) {
            Logger.error(e2);
            return null;
        }
        catch (InvocationTargetException e3) {
            Logger.error(e3);
            return null;
        }
        catch (Exception e4) {
            Logger.error(e4);
            return null;
        }
        finally {
            Logger.sysInfo(String.format("[%1$-20s][%2$-10s][Execute. Class '%3$s'. Method '%4$s'. Return.type '%5$s'] - %6$s", "DCLASS.LDR", (result != null) ? "RUNNING" : "ERROR", (classname != null) ? classname : "N/A", (methodName != null) ? methodName : "N/A", (returnType != null && returnType.getName() != null) ? returnType.getName() : "N/A", (result != null) ? "SUCCESS" : "FAIL"), LoggerElements.LOG_LEVEL3);
        }
    }
    
    public static void main(final String[] args) {
        try {
            final DynamicClassLoader cl = new DynamicClassLoader();
            LoggerElements.setLevel(3);
            for (int i = 0; i < 10; ++i) {
                cl.loadFile("./lib/eai.test.jar");
                cl.loadClassesAll();
                cl.execute("module.test.eai.InitEAI", "init", new Object[0]);
                final Runtime runtime = Runtime.getRuntime();
                final long usedMemSize = runtime.totalMemory() - runtime.freeMemory();
                System.out.println("Debug index[" + i + "][class.map.size : " + cl.srcClassMap.size() + "][ class.instance.map : " + cl.srcClassInstanceMap.size() + ", heap size : " + String.valueOf(usedMemSize));
                Thread.sleep(1L);
            }
        }
        catch (Exception e) {
            e.printStackTrace();
        }
    }
}
