import React from "react";
import Pipeline from "../../pipeline";

const Edit = props => {
  const {
    location: {
      state: {
        id,
        value: { configuration, trigger, ...rest }
      }
    }
  } = props;
  return (
    <Pipeline
      id={id}
      value={{
        configuration: [trigger, ...configuration],
        ...rest
      }}
    />
  );
};

export default Edit;
