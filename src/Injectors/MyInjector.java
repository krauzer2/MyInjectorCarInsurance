package Injectors;

import java.io.File;
import java.net.URL;
import java.util.*;

public class MyInjector {

    private Map<Class<?>, Object> mySingletonObjects = new HashMap<>();

    private Object createInstance(Class<?> type) throws Exception {

        if(mySingletonObjects.containsKey(type))
            throw new IllegalArgumentException("Singleton object cannot be reinstantiated");

        if (type.isInterface())
        {
            type = findClassesForInterface(type).get(0);
        }

        List<Object> params = new ArrayList<Object>();

        for(var constructor : type.getConstructors()){
            var parameters =constructor.getParameters();

            //loop through all constructor arguments and create instances of dependencies.
            for(var parameter: parameters){
                var typeToInstantiate = parameter.getType();
                params.add(createInstance(typeToInstantiate));
            }
            return params.size() > 0 ? constructor.newInstance(params.toArray()) : constructor.newInstance();
        }
        return null;
    }

    private List<Class<?>> findClassesForInterface(Class<?> type) throws Exception {
        if (!type.isInterface())
            throw new IllegalArgumentException(String.format("not an interface %s",type.getName()));

        List<Class<?>> classes = new ArrayList<>();

        var classLoader = Thread.currentThread().getContextClassLoader();
        var path = type.getPackageName().replace('.','/');
        Enumeration<URL> resources = classLoader.getResources(path);

        //loop through resources to retrieve the class files
        while (resources.hasMoreElements()) {
            URL resource = resources.nextElement();
            File file = new File(resource.getFile());
            classes.addAll(findClasses(file, type, type.getPackageName()));
        }

        return classes;
    }

    private static List<Class<?>> findClasses(File directory, Class<?> type, String packageName) throws ClassNotFoundException {
        List<Class<?>> classes = new ArrayList<>();
        if (!directory.exists()) {
            return classes;
        }

        File[] files = directory.listFiles();
        for (File file : files) {
            if (file.isDirectory()) {
                classes.addAll(findClasses(file, type, packageName + "." + file.getName()));
            } else if (file.getName().endsWith(".class")) {
                String className = packageName + '.' + file.getName().substring(0, file.getName().length() - 6);
                Class<?> retrievedClass = Class.forName(className);

                //check if class implements interface.
                if(type.isAssignableFrom(retrievedClass) && !retrievedClass.isInterface())
                    classes.add(retrievedClass);
            }
        }

        return classes;
    }



    public <T> T singletonOf(Class<?> type) throws Exception {
        Object result = mySingletonObjects.get(type);
        if (result != null) {
            return (T)result;
        }

        Object newSingletonObject = createInstance(type);
        mySingletonObjects.put(type, newSingletonObject);
        return (T)newSingletonObject;
    }

    public <T> T oneOf(Class<?> type) throws Exception {
        return (T)createInstance(type);
    }

    public <T> List<T> listOf(Class<?> iface) throws Exception {
        List<T> result = new ArrayList<>();
        for (Class<?> type : findClassesForInterface(iface)) {
            result.add((T)createInstance(type));
        }
        return result;
    }

}
