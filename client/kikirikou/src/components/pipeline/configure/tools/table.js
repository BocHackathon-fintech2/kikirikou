import React from "react";
import ColumnsList from "./columnslist";

const Table = ({ imports, values }) => (
  <ColumnsList name="columns" columns={imports} values={values} />
);

export default Table;
