import React from "react";
import ColumnsList from "./columnslist";
import Fieldset from "../../../fieldset";

const Table = ({ imports, values }) => (
  <>
    <Fieldset name="phones" />
    <Fieldset name="text" component="textarea" rows="5" />
    <ColumnsList name="columns" columns={imports} values={values} />
  </>
);

export default Table;
