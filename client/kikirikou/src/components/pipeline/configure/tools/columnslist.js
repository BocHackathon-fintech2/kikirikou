import React from "react";
import { FieldArray } from "formik";

const ColumnsList = ({ name, columns, values }) => {
  return (
    <FieldArray
      name={name}
      render={arrayHelpers => (
        <div className="form-group">
          <label>{name}</label>
          <div className="list-group list-group-flush">
            {columns.map(key => (
              <div
                key={key}
                className="list-group-item align-items-start pl-0 pr-0 d-flex justify-content-between"
              >
                <label htmlFor={key} className="mb-0">
                  {key}
                </label>
                <input
                  name={key}
                  type="checkbox"
                  value={key}
                  checked={values[name].includes(key)}
                  onChange={e => {
                    if (e.target.checked) arrayHelpers.push(key);
                    else {
                      const idx = values[name].indexOf(key);
                      arrayHelpers.remove(idx);
                    }
                  }}
                />
              </div>
            ))}
          </div>
        </div>
      )}
    />
  );
};

export default ColumnsList;
