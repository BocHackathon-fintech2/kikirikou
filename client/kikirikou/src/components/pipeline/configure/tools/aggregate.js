import React from "react";
import Fieldset from "../../../fieldset";
import ColumnsList from "./columnslist";

const EXECUTES = ["SUM", "COUNT", "MIN", "MAX", "AVERAGE"];

const Aggregate = ({ imports, values }) => (
  <>
    <ColumnsList name="columns" columns={imports} values={values} />
    <Fieldset component="select" name="execute">
      <option value="" />
      {EXECUTES.map(execute => (
        <option value={execute} key={execute}>
          {execute}
        </option>
      ))}
    </Fieldset>

    <Fieldset component="select" name="groupBy">
      <option value="" />
      {imports.map(key => (
        <option value={key} key={key}>
          {key}
        </option>
      ))}
    </Fieldset>
  </>
);

export default Aggregate;
