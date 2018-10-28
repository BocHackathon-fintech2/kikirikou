import React from "react";
import Fieldset from "../../../fieldset";

const OPERATORS = {
  "Greater than": ">",
  "Greater than or equals": ">=",
  "Less than": "<",
  "Less than or equals": "<=",
  Equals: "=",
  "Not equals": "!="
};

const Filter = ({ imports }) => (
  <>
    <Fieldset component="select" name="column">
      <option value="" />
      {imports.map(key => (
        <option value={key} key={key}>
          {key}
        </option>
      ))}
    </Fieldset>
    <Fieldset component="select" name="op">
      <option value="" />
      {Object.entries(OPERATORS).map(([key, value]) => (
        <option value={value} key={value}>
          {key}
        </option>
      ))}
    </Fieldset>
    <Fieldset name="value" />
  </>
);

export default Filter;
