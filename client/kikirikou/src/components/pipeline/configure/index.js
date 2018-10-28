import React from "react";
import { Formik } from "formik";
import tools from "./tools";
import { tools as toolsList } from "../../other/tools";

class Configure extends React.Component {
  render() {
    const { tool, onClose, onSave, onDelete, imports } = this.props;
    const { type, config } = tool;
    const ConfigurationComponent = tools[type];
    return (
      <Formik
        initialValues={config}
        validationSchema={toolsList[type].validationSchema}
        onSubmit={values => onSave(values)}
      >
        {({ handleSubmit, isSubmitting, values }) => (
          <form className="d-flex flex-column h-100" onSubmit={handleSubmit}>
            <div className="d-flex border-bottom p-3 justify-content-between">
              <h5 className="mb-0">{type}</h5>
              <button
                type="button"
                className="close"
                aria-label="Close"
                onClick={() => onClose()}
              >
                <span aria-hidden="true">&times;</span>
              </button>
            </div>

            <div className="flex-grow-1 p-3" style={{ overflow: "auto" }}>
              {ConfigurationComponent && (
                <ConfigurationComponent
                  {...tool}
                  onSave={onSave}
                  values={values}
                  imports={imports}
                />
              )}
            </div>
            <div className="p-3 border-top">
              <div className="btn-group w-100">
                <button
                  type="submit"
                  className="btn btn-primary w-50"
                  disabled={isSubmitting}
                >
                  Save
                </button>
                <button
                  disabled={!Boolean(onDelete) || isSubmitting}
                  type="button"
                  className="btn btn-danger w-50"
                  onClick={() => onDelete()}
                >
                  Delete
                </button>
              </div>
            </div>
          </form>
        )}
      </Formik>
    );
  }
}

export default Configure;
