

export const mkTopic = (initialValue) => {

  const subscribers = [];
  let value = initialValue;

  return {

    subscribe: (callback) => {

      const index = subscribers.push(callback) - 1;

      if (value) {
        callback(value);
      }

      return {
        dispose: () => {
          delete subscribers[index];
        }
      };
    },

    publish: (v) => {
      value = v;
      subscribers.forEach(s => s(v))
    },

    getValue: () => {
      return value;
    }

  }

};
