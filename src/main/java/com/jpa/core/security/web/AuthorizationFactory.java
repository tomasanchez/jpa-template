package com.jpa.core.security.web;

import java.lang.reflect.Method;
import java.util.Arrays;
import java.util.Collection;
import java.util.List;
import java.util.stream.Collectors;
import com.jpa.core.mvc.controller.Controller;
import com.jpa.core.mvc.controller.routing.DeleteMapping;
import com.jpa.core.mvc.controller.routing.GetMapping;
import com.jpa.core.mvc.controller.routing.PostMapping;
import com.jpa.core.mvc.controller.routing.PutMapping;
import com.jpa.core.security.auth.Secured;
import com.jpa.core.security.filter.SparkAuthorizationFilter;
import com.jpa.core.security.userdetails.GrantedAuthority;
import com.jpa.core.security.userdetails.SimpleGrantedAuthority;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import spark.Spark;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class AuthorizationFactory {

    Controller controller;

    SparkAuthorizationFilter filter;

    public Collection<Method> obtainSecuredMethods() {
        return Arrays.stream(getController().getClass().getDeclaredMethods())
                .filter(m -> m.isAnnotationPresent(Secured.class)).collect(Collectors.toSet());
    }

    public void secureMethods() {
        obtainSecuredMethods().stream().forEach(m -> {

            Secured secured = m.getAnnotation(Secured.class);
            List<GrantedAuthority> authorities = Arrays.stream(secured.roles())
                    .map(SimpleGrantedAuthority::new).collect(Collectors.toList());

            secureGetMapping(m, authorities);
            securePostMapping(m, authorities);
            securePutMapping(m, authorities);
            secureDeleteMapping(m, authorities);

        });
    }

    private void secureGetMapping(Method method, Collection<GrantedAuthority> authorities) {

        GetMapping getMapping = method.getAnnotation(GetMapping.class);

        if (getMapping == null) {
            return;
        }

        secureMapping(getMapping.path(), authorities);

    }

    private void securePostMapping(Method method, Collection<GrantedAuthority> authorities) {

        PostMapping getMapping = method.getAnnotation(PostMapping.class);

        if (getMapping == null) {
            return;
        }

        secureMapping(getMapping.path(), authorities);

    }

    private void securePutMapping(Method method, Collection<GrantedAuthority> authorities) {

        PutMapping getMapping = method.getAnnotation(PutMapping.class);

        if (getMapping == null) {
            return;
        }

        secureMapping(getMapping.path(), authorities);

    }

    private void secureDeleteMapping(Method method, Collection<GrantedAuthority> authorities) {

        DeleteMapping getMapping = method.getAnnotation(DeleteMapping.class);

        if (getMapping == null) {
            return;
        }

        secureMapping(getMapping.path(), authorities);

    }


    private void secureMapping(String path, Collection<GrantedAuthority> authorities) {

        if (path == null || path.isEmpty()) {
            path = controller.getEndPoint();
        }

        Spark.before(path, (req, res) -> {
            filter.authorizationFilter(req, res, authorities);
        });
    }

}
