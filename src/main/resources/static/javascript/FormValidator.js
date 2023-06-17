export default class FormValidator {
  constructor(selector) {
    this.form = document.querySelector(selector);
    this.inputsWithError = new Set();

    this.form.addEventListener("submit", (e) => {
      e.preventDefault();
      if (!this.hasErrors) {
        this.form.submit();
      }
    });
  }

  get hasErrors() {
    return this.inputsWithError.size > 0;
  }

  register(selector, check) {
    console.log("check");
    const inputField = this.form.querySelector(selector);
    const errorElement = this.form.querySelector(".subject_error");
    const execute = (flag) => {
      const { pass, error } = check(inputField.value, inputField);
      if(!flag){
        errorElement.textContent = error || "";
      }
      if (!pass) {
        this.inputsWithError.add(inputField);
      }
      else{
        this.inputsWithError.delete(inputField);
      }
      console.log(pass);
      console.log(error);
    };

    inputField.addEventListener("change", () => execute);
    execute(true);
  }
}
