/**
 * LogIn.js
 *
 * @file  LogIn page Scripting
 * @author Tomás Sánchez
 * @since  03.14.2022
 */

onShowPassowrd = () => {
  let passwordInput = document.getElementById("passwordInput"),
    showPasswordIcon = document.getElementById("showPasswordIcon");

  var isShown = passwordInput.getAttribute("type") === "text";

  passwordInput.setAttribute("type", isShown ? "password" : "text");

  showPasswordIcon.classList.remove(isShown ? "fa-eye-slash" : "fa-eye");
  showPasswordIcon.classList.add(isShown ? "fa-eye" : "fa-eye-slash");
};
