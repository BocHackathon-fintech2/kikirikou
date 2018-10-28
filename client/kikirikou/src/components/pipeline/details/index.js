import React from "react";
import "./assets/style.css";
import { tools as toolsList } from "../../other/tools";
import ReactCSSTransitionGroup from "react-addons-css-transition-group"; // ES6
import classNames from "classnames";

const Item = ({ tool: { type, hasError }, onClick, selected }) => {
  const { icon, description } = toolsList[type];
  return (
    <div className="row no-gutters justify-content-end justify-content-md-around align-items-start  timeline-nodes">
      <div className="col-10 col-md-5 order-3 order-md-1 timeline-content bg-white text-body">
        <h5 className="text-light">{type}</h5>
        {hasError ? (
          <p>{description}</p>
        ) : (
          <div className="container-fluid">
            <dl className="row mb-0">
              <dt className="col-sm-3">Description lists</dt>
              <dd className="col-sm-9">
                A description list is perfect for defining terms.
              </dd>
            </dl>
          </div>
        )}
      </div>
      <button
        className={classNames(
          "btn btn-link col-2 col-sm-1 px-md-3 order-2 timeline-image text-md-center",
          { active: selected, "has-error": hasError }
        )}
        onClick={onClick}
      >
        <img src={icon} className="img-fluid grow" alt="img" />
      </button>
      <div className="col-10 col-md-5 order-1 order-md-3 py-3 timeline-date" />
    </div>
  );
};

const Details = ({ tools, onSelect, selectedIndex }) => {
  return tools.length === 0 ? (
    <div className="d-flex flex-column h-100 align-items-center">
      <div className="card mt-3 w-75">
        <div className="card-header">Configure pipeline</div>
        <div className="card-body">
          <p className="card-text">
            Lorem Ipsum is simply dummy text of the printing and typesetting
            industry. Lorem Ipsum has been the industry's standard dummy text
            ever since the 1500s, when an unknown printer took a galley of type
            and scrambled it to make a type specimen book. It has survived not
            only five centuries, but also the leap into electronic typesetting,
            remaining essentially unchanged. It was popularised in the 1960s
            with the release of Letraset sheets containing Lorem Ipsum passages,
            and more recently with desktop publishing software like Aldus
            PageMaker including versions of Lorem Ipsum.
          </p>
        </div>
      </div>
    </div>
  ) : (
    <div className="timeline">
      <ReactCSSTransitionGroup
        transitionName="example"
        transitionEnterTimeout={500}
        transitionLeaveTimeout={300}
        transitionAppear={true}
        transitionAppearTimeout={500}
      >
        {tools.map((tool, index) => (
          <Item
            key={index}
            tool={tool}
            selected={selectedIndex === index}
            onClick={() => {
              onSelect(index);
            }}
          />
        ))}
      </ReactCSSTransitionGroup>
    </div>
  );
};

export default Details;
