package de.brokenstudio.hermes.rest;

import de.brokenstudio.hermes.rest.annotations.*;
import de.brokenstudio.hermes.util.AppAccessor;
import io.javalin.Javalin;

import java.lang.reflect.Method;

public class RestManager implements AppAccessor {

    private Javalin app;

    public RestManager(){
        app = Javalin.create();
        internalControllers();
        app.start(config().getApplication().getHost(), config().getApplication().getPort());
    }

    private void internalControllers(){

    }

    public void addController(Object object){
        if(!object.getClass().isAnnotationPresent(Controller.class)){
            System.out.println("Controller annotation not found");
            return;
        }
        String path = object.getClass().getAnnotation(Controller.class).value();
        for (Method declaredMethod : object.getClass().getDeclaredMethods()) {
            if(declaredMethod.isAnnotationPresent(Get.class)){
                Get get = declaredMethod.getAnnotation(Get.class);
                app.get(path + get.value(), ctx -> declaredMethod.invoke(object, ctx), get.access());
            }else if(declaredMethod.isAnnotationPresent(Post.class)){
                Post post = declaredMethod.getAnnotation(Post.class);
                app.post(path + post.value(), ctx -> declaredMethod.invoke(object, ctx), post.access());
            }else if(declaredMethod.isAnnotationPresent(Patch.class)){
                Patch patch = declaredMethod.getAnnotation(Patch.class);
                app.patch(path + patch.value(), ctx -> declaredMethod.invoke(object, ctx), patch.access());
            }else if(declaredMethod.isAnnotationPresent(Put.class)){
                Put put = declaredMethod.getAnnotation(Put.class);
                app.put(path + put.value(), ctx -> declaredMethod.invoke(object, ctx), put.access());
            }else if(declaredMethod.isAnnotationPresent(Delete.class)){
                Delete delete = declaredMethod.getAnnotation(Delete.class);
                app.delete(path + delete.value(), ctx -> declaredMethod.invoke(object, ctx), delete.access());
            }
        }
    }

}
