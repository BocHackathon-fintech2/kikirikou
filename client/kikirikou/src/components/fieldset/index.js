import React from "react";
import { Field } from "formik";
import classNames from "classnames";

const Fieldset = ({
  component = "input",
  render,
  name,
  label = name,
  validate,
  ...rest
}) => (
  <Field
    name={name}
    validate={validate}
    render={({ field, form }) => {
      const error = form.touched[name] && form.errors[name];
      return render ? (
        render({ field, form, error, ...rest }) // render prop inception
      ) : component ? (
        <div className="form-group">
          <label htmlFor={name}>{label}</label>
          {React.createElement(component, {
            ...field,
            ...rest,
            className: classNames("form-control", { "is-invalid": error }),
            invalid: (!!error).toString()
          })}
          <div className="invalid-feedback">{error}</div>
        </div>
      ) : null;
    }}
  />
);

export default Fieldset;
