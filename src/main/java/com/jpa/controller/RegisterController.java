package com.jpa.controller;

import java.util.Optional;
import com.jpa.core.mvc.controller.routing.GetMapping;
import com.jpa.core.mvc.controller.routing.PostMapping;
import com.jpa.exceptions.user.UserAlreadyExistsException;
import com.jpa.model.user.User;
import com.jpa.services.UserService;
import spark.ModelAndView;
import spark.Request;
import spark.Response;

public class RegisterController extends BaseController {

    private UserService userService = new UserService();

    /* =========================================================== */
    /* Lifecycle methods ----------------------------------------- */
    /* =========================================================== */

    @Override
    protected void onInit() {
        // TODO Auto-generated method stub

    }

    @Override
    protected void onBeforeRendering(Request request, Response response) {

        // Do not allow request when already logged in.
        if (isLogged(request)) {
            navTo(response, "home");
        }

    }

    @Override
    protected void onAfterRendering(Request request, Response response) {}

    /* =========================================================== */
    /* Request Handling ------------------------------------------ */
    /* =========================================================== */

    @GetMapping
    private ModelAndView index(Request request, Response response) {
        return getModelAndView();
    }

    @PostMapping
    private ModelAndView onRegister(Request request, Response response) {

        String username = request.queryParams("username");
        String password = request.queryParams("password");

        String confirmPassword = request.queryParams("confirmPassword");

        if (!confirmPassword.equals(password)) {
            togglePasswordValidation(false);
            return getModelAndView();
        }

        onSaveUser(new User(username, password)).ifPresent(u -> {
            navTo(response, "login");
        });

        return getModelAndView();
    }

    /* =========================================================== */
    /* Internal Methods ------------------------------------------ */
    /* =========================================================== */

    /**
     * Attempts saving the user.
     * 
     * @param user the user to be saved
     * @return the persisted user reference
     */
    private Optional<User> onSaveUser(User user) {

        try {
            return Optional.of(userService.save(user));
        } catch (UserAlreadyExistsException uae) {
            toggleUserValidation(false);
            return Optional.empty();
        } catch (Exception e) {
            return Optional.empty();
        }
    }

    /**
     * Toggles invalid feedback on user input.
     * 
     * @param valid if true sets to neutral, otherwise sets to invalid
     */
    private void toggleUserValidation(boolean valid) {
        getView().getModel().set("isValidUser", valid ? "" : "is-invalid");
    }

    /**
     * Toggles invalid feedback on password input.
     * 
     * @param valid if true sets to neutral, otherwise sets to invalid
     */
    private void togglePasswordValidation(boolean valid) {
        getView().getModel().set("isValidPassword", valid ? "" : "is-invalid");
    }

}
