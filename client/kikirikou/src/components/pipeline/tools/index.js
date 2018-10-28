import React from "react";
import classNames from "classnames";
import { tools as toolsList } from "../../other/tools";

const Tools = ({ tools, selectTool }) => (
  <div className="list-group list-group-flush">
    {tools.map(({ name, description }) => {
      const { icon } = toolsList[name];
      return (
        <button
          key={name}
          className={classNames(
            "list-group-item",
            "list-group-item-action",
            "flex-column",
            "align-items-start"
          )}
          onClick={() => selectTool(name)}
        >
          <div className="d-flex">
            <img
              src={icon}
              className="img-thumbnail mr-3"
              alt="img"
              style={{ width: 50, height: 50 }}
            />
            <div className="d-flex flex-grow-1 flex-column justify-content-between">
              <h5 className="mb-0">{name}</h5>
              <p className="mb-0">{description}</p>
            </div>
          </div>
        </button>
      );
    })}
  </div>
);

export default Tools;
