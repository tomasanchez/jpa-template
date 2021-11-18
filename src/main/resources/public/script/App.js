/**
 * App.js
 *
 * @file  <DESCRIPTION>
 * @author Tomás Sánchez
 * @since  11.18.2021
 */

/**
 * Boostrap's Toast List
 *
 * @type boostrapToast
 */
var toastList = [];

/**
 * When document init all toasts.
 */
onInit = function () {
  console.log("READY!");
  var toastElList = [].slice.call(document.querySelectorAll(".toast"));

  toastList = toastElList.map(function (toastEl) {
    return new bootstrap.Toast(toastEl);
  });
};

function showToasts(show = false) {
  if (show) {
    toastList.forEach((toast) => toast.show());
  }
}
