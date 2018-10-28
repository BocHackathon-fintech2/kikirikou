import { object, string, date } from "yup";
import support from "./assets/support.svg";
import clock from "./assets/clock-circular-outline.svg";
import pay from "./assets/pay.svg";
import statement from "./assets/file.svg";
import sms from "./assets/speech-bubble.svg";
import table from "./assets/table-grid.svg";
import aggregate from "./assets/merge.svg";
import filter from "./assets/filter-filled-tool-symbol.svg";

export const state = {
  TRIGGER: Symbol("trigger"),
  PROCESSOR: Symbol("processor")
};

const TRANSACTION_OUTPUT = [
  "id",
  "dcInd",
  "transactionAmount.amount",
  "transactionAmount.currency",
  "description",
  "postingDate",
  "valueDate",
  "transactionType",
  "merchant.name",
  "merchant.terminalId"
];

export const tools = {
  cron: {
    description: "test",
    when: state.TRIGGER,
    icon: clock,
    configuration: {
      schedule: ""
    },
    exports: ["executed"]
  },
  transaction: {
    description: "test",
    when: state.TRIGGER,
    icon: support,
    configuration: {
      account: ""
    },
    validationSchema: object().shape({
      account: string()
        .trim()
        .required()
    }),
    exports: TRANSACTION_OUTPUT
  },
  statement: {
    description: "hello2",
    when: state.PROCESSOR,
    icon: statement,
    configuration: {
      account: "",
      from: "",
      to: ""
    },
    validationSchema: object().shape({
      account: string()
        .trim()
        .required(),
      from: date().required(),
      to: date().required()
    }),
    exports: TRANSACTION_OUTPUT
  },
  aggregate: {
    description: "hello2",
    when: state.PROCESSOR,
    icon: aggregate,
    configuration: {
      groupBy: "",
      columns: [],
      execute: ""
    },
    validationSchema: object().shape({
      execute: string()
        .trim()
        .required()
    }),
    exports: ["key", "value"]
  },
  filter: {
    description: "hello2",
    when: state.PROCESSOR,
    icon: filter,
    configuration: {
      column: "",
      op: "",
      value: ""
    },
    validationSchema: object().shape({
      column: string()
        .trim()
        .required(),
      op: string()
        .trim()
        .required(),
      value: string()
        .trim()
        .required()
    })
  },
  table: {
    description: "hello2",
    when: state.PROCESSOR,
    icon: table,
    configuration: {
      columns: []
    },
    exports: ["value"]
  },
  sms: {
    description: "hello2",
    when: state.PROCESSOR,
    icon: sms,
    configuration: {
      columns: [],
      phones: "",
      text: ""
    },
    validationSchema: object().shape({
      phones: string()
        .trim()
        .required(),
      text: string()
        .trim()
        .required()
    })
  },
  payment: {
    description: "hello2",
    when: state.PROCESSOR,
    icon: pay,
    configuration: {
      from: "",
      to: "",
      amount: "",
      type: ""
    },
    validationSchema: object().shape({
      from: string()
        .trim()
        .required(),
      to: string()
        .trim()
        .required(),
      amount: string()
        .trim()
        .required(),
      type: string()
        .trim()
        .required()
    })
  }
};
