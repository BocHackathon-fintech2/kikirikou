import React from "react";
import { Formik } from "formik";
import Fieldset from "../../fieldset";
import { object, string } from "yup";

import run from "./assets/run.svg";
import remove from "./assets/delete.svg";

const VALIDATION_SCHEMA = object().shape({
  name: string()
    .trim()
    .required(),
  notes: string().trim()
});

class Overview extends React.Component {
  render() {
    const { onClear, onRun, onSave } = this.props;
    return (
      <Formik
        initialValues={{
          name: this.props.name,
          notes: this.props.notes
        }}
        validationSchema={VALIDATION_SCHEMA}
        onSubmit={(values, { setSubmitting }) => onSave(values, setSubmitting)}
      >
        {({ handleSubmit, isSubmitting, values }) => (
          <form className="d-flex flex-column h-100" onSubmit={handleSubmit}>
            <div className="flex-grow-1 p-3" style={{ overflow: "auto" }}>
              <Fieldset name="name" />
              <Fieldset name="notes" component="textarea" rows="10" />
            </div>
            <div className="p-3 border-top">
              <div className="row no-gutters">
                <div className="col mr-3">
                  <button
                    type="submit"
                    className="btn btn-primary btn-block"
                    disabled={isSubmitting || !Boolean(onSave)}
                  >
                    Save
                  </button>
                </div>
                <div className="col">
                  <div className="btn-group w-100">
                    <button
                      type="button"
                      className="btn btn-success w-50"
                      disabled={isSubmitting || !Boolean(onRun)}
                      onClick={onRun}
                    >
                      <img src={run} alt="img" style={{ width: 20 }} />
                    </button>
                    <button
                      className="btn btn-warning w-50"
                      disabled={isSubmitting || !Boolean(onClear)}
                      onClick={onClear}
                    >
                      <img src={remove} alt="img" style={{ width: 20 }} />
                    </button>
                  </div>
                </div>
              </div>
            </div>
          </form>
        )}
      </Formik>
    );
  }
}

export default Overview;
