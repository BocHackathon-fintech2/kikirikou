import React from "react";
import memoize from "memoize-one";
import Tools from "./tools";
import Configure from "./configure";
import { state, tools } from "../other/tools";
import Details from "./details";
import Overview from "./overview";
import { navigate } from "@reach/router";

const INITIAL_STATE = {
  selectedToolIndex: null,
  currentState: state.TRIGGER,
  tools: []
};

const URL = "/api/pipeline";
const STUB = {
  cron: {
    executed: new Date().toString()
  },
  transaction: {
    id: "12345",
    dcInd: "CY123",
    merchant: {
      name: "DEMETRIS KOSTA",
      address: {
        city: "CITY",
        line1: "A-123",
        line2: "APARTMENT",
        line3: "STREET",
        line4: "AREA",
        state: "STATE",
        country: "CYPRUS",
        postalcode: "CY-01"
      }
    },
    valueDate: "2018-10-24",
    terminalId: "12345",
    description: "NEFT TRANSACTION",
    postingDate: "2018-10-24",
    transactionType: "CASH",
    transactionAmount: {
      amount: 563.91,
      currency: "EUR"
    }
  }
};

const postData = (path = "/", data = {}, method = "POST") => {
  return fetch(`${URL}${path}`, {
    method,
    mode: "cors", // no-cors, cors, *same-origin
    cache: "no-cache", // *default, no-cache, reload, force-cache, only-if-cached
    credentials: "same-origin", // include, same-origin, *omit
    headers: {
      "Content-Type": "application/json; charset=utf-8"
    },
    redirect: "follow", // manual, *follow, error
    referrer: "no-referrer", // no-referrer, *client
    body: JSON.stringify(data) // body data type must match "Content-Type" header
  }).then(response => {
    if (!response.ok) throw Error(response.statusText);

    return response;
  });
};

class Create extends React.Component {
  static defaultProps = {
    value: null
  };

  constructor(props) {
    super(props);
    if (this.props.value !== null) {
      const { name, notes, configuration } = this.props.value;
      this.state = {
        name,
        notes,
        selectedToolIndex: null,
        currentState:
          configuration.length > 0 ? state.PROCESSOR : state.TRIGGER,
        tools: configuration.map(configuration => ({
          ...configuration,
          hasError: false
        }))
      };
    } else {
      this.state = {
        name: "",
        notes: "",
        ...INITIAL_STATE
      };
    }
  }

  selectTool = tool => {
    const { configuration } = tools[tool];
    this.setState(({ tools: currentTools, currentState }) => ({
      tools: [
        ...currentTools,
        {
          type: tool,
          config: configuration,
          hasError: Boolean(configuration)
        }
      ],
      selectedToolIndex: currentTools.length,
      currentState:
        currentState === state.TRIGGER ? state.PROCESSOR : currentState
    }));
  };

  deleteTool = () => {
    this.setState(({ selectedToolIndex, tools: currentTools }) => {
      currentTools.splice(selectedToolIndex, 1);
      return {
        tools: currentTools,
        selectedToolIndex: null,
        currentState:
          currentTools.length === 0 ? state.TRIGGER : state.PROCESSOR
      };
    });
  };

  closeTool = () => {
    this.setState({
      selectedToolIndex: null
    });
  };

  saveTool = values =>
    this.setState(({ selectedToolIndex, tools: currentTools }) => {
      const tool = currentTools[selectedToolIndex];
      currentTools[selectedToolIndex] = {
        ...tool,
        config: values,
        hasError: false
      };
      return {
        tools: currentTools,
        selectedToolIndex: null
      };
    });

  selectIndex = index =>
    this.setState(({ selectedToolIndex }) => ({
      selectedToolIndex: selectedToolIndex === index ? null : index
    }));

  getSelectionTools = memoize(state => {
    return Object.entries(tools)
      .filter(([key, { when }]) => when === state)
      .map(([key, { description }]) => ({
        name: key,
        description
      }));
  });

  getImports = memoize(selectedToolIndex => {
    const { tools: currentTools } = this.state;
    let index = selectedToolIndex;
    while (index > 0) {
      index -= 1;
      const exports = tools[currentTools[index].type].exports;
      if (Boolean(exports)) return exports;
    }

    return null;
  });

  canSave = tools =>
    tools.length > 0 && tools.find(({ hasError }) => hasError) === undefined;

  toConfig = tools => {
    const toSaveTools = tools.map(({ hasError, config, ...rest }) => ({
      config,
      ...rest
    }));
    const trigger = toSaveTools[0];
    const configuration = toSaveTools.filter((element, index) => index >= 1);
    return {
      trigger,
      configuration
    };
  };

  onSave = (values, setSubmitting) => {
    const { tools } = this.state;
    const pipeline = {
      ...values,
      ...this.toConfig(tools)
    };
    const { id } = this.props;

    postData(id ? `/${id}` : "", pipeline, id ? "PUT" : "POST")
      .then(response => response.text())
      .then(text => navigate("/"))
      .catch(ex => setSubmitting(false));
  };

  onRun = () => {
    const { tools } = this.state;

    const {
      trigger: { type },
      configuration
    } = this.toConfig(tools);

    const data = {
      data: [STUB[type]],
      config: configuration
    };

    postData("/run", data).catch(ex => {
      console.log("Error running", ex);
    });
  };

  onClear = () => {
    this.setState(state => ({
      ...state,
      ...INITIAL_STATE
    }));
  };

  render() {
    const {
      selectedToolIndex,
      tools: currentTools,
      currentState,
      name,
      notes
    } = this.state;
    const canSave = this.canSave(currentTools);
    return (
      <div style={{ width: "100vw", height: "100vh" }}>
        <div className="row h-100 no-gutters">
          <div className="col-3 bg-white border-right">
            <Overview
              name={name}
              notes={notes}
              onClear={currentTools.length > 0 ? this.onClear : null}
              onSave={canSave ? this.onSave : null}
              onRun={canSave ? this.onRun : null}
            />
          </div>
          <div
            className="col-6"
            style={{
              height: "100vh",
              overflow: "auto"
            }}
          >
            <Details
              tools={currentTools}
              onSelect={this.selectIndex}
              selectedIndex={selectedToolIndex}
            />
          </div>
          <div className="col-3 bg-white border-left">
            {selectedToolIndex === null ? (
              <Tools
                tools={this.getSelectionTools(currentState)}
                selectTool={this.selectTool}
              />
            ) : (
              <Configure
                key={selectedToolIndex}
                tool={currentTools[selectedToolIndex]}
                imports={this.getImports(selectedToolIndex)}
                onClose={this.closeTool}
                onDelete={
                  selectedToolIndex === currentTools.length - 1
                    ? this.deleteTool
                    : null
                }
                onSave={this.saveTool}
              />
            )}
          </div>
        </div>
      </div>
    );
  }
}

export default Create;
