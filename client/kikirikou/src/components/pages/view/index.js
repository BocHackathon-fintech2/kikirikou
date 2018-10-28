import React from "react";
import { tools as toolsList } from "../../other/tools";
import classNames from "classnames";
import { Link } from "@reach/router";

const URL = "/api/pipeline";

const getData = (path = "/") => {
  return fetch(`${URL}${path}`, {
    mode: "cors", // no-cors, cors, *same-origin
    cache: "no-cache", // *default, no-cache, reload, force-cache, only-if-cached
    credentials: "same-origin", // include, same-origin, *omit
    redirect: "follow", // manual, *follow, error
    referrer: "no-referrer" // no-referrer, *client
  }).then(response => {
    if (!response.ok) throw Error(response.statusText);

    return response;
  });
};

const ImageDescription = ({ configuration }) => (
  <div className="d-flex">
    {configuration.map(({ type }, index) => (
      <img
        key={index}
        src={toolsList[type].icon}
        alt="icon"
        className="img-thumbnail ml-3"
        style={{ width: 50, height: 50 }}
      />
    ))}
  </div>
);

const List = ({ pipelines }) => {
  return (
    <div className="list-group list-group-flush">
      {Object.entries(pipelines).map(([key, value]) => {
        const { name, notes, trigger, configuration } = value;
        return (
          <Link
            to="/edit"
            state={{ id: key, value }}
            key={key}
            className={classNames(
              "list-group-item",
              "list-group-item-action",
              "flex-column",
              "align-items-start"
            )}
          >
            <div className="d-flex justify-content-between">
              <div>
                <h5 className="mb-1">{name}</h5>
                <p className="mb-1">{notes || "No notes added"}</p>
              </div>
              <div className="mt-1">
                <ImageDescription configuration={[trigger, ...configuration]} />
              </div>
            </div>
          </Link>
        );
      })}
    </div>
  );
};

class View extends React.Component {
  state = {
    pipelines: null
  };
  componentDidMount() {
    getData()
      .then(response => response.json())
      .then(data => this.setState({ pipelines: data }));
  }

  render() {
    const { pipelines } = this.state;

    return (
      <div className="container">
        <div className="card mt-3">
          <div className="card-header">
            <div className="d-flex justify-content-between align-items-center">
              <h3 className="mb-0">My workflows</h3>
              <Link to="/create" className="btn btn-primary">
                New workflow
              </Link>
            </div>
          </div>
          <div className="card-body p-0">
            {pipelines === null ? "Loading" : <List pipelines={pipelines} />}
          </div>
        </div>
      </div>
    );
  }
}

export default View;
