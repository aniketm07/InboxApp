import FormValidator from "./FormValidator.js";
const fv = new FormValidator("#composeEmail");

fv.register("#subject", (value, inputField)=>{
    if(value=="" || value==null){
        return {
            pass : false,
            error : "Subject must not be null/blank"
        }
    }

    return {
        pass: true
    }
});

window.fv = fv;