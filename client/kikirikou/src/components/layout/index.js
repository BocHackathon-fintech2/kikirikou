import React from "react";
import Create from "../pages/create";
import View from "../pages/view";
import Edit from "../pages/edit";
import { Router } from "@reach/router";

import "./assets/bootstrap.css";
import "./assets/layout.css";

const Layout = () => (
  <Router>
    <View path="/" />
    <Create path="/create" />
    <Edit path="/edit" />
  </Router>
);

export default Layout;
