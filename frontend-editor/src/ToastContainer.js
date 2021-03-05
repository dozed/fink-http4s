import Toast from "react-bootstrap/Toast";
import React, {useEffect, useState} from "react";
import {mkTopic} from "util/topic";

const toastTopic = mkTopic();

const mkToastId = () => Math.random()
  .toString(36)
  .substr(2);

const mkToast = (header, content) => {
  const toastId = mkToastId();

  const toast = {
    id: toastId,
    header,
    content,
  };

  return toast;
};

export const addToast = (header, content) => {
  const toast = mkToast(header, content);
  toastTopic.publish(toast);
}

export const ToastContainer = () => {

  const [toasts, setToasts] = useState([]);

  useEffect(() => {
    const toastTopicSub = toastTopic.subscribe(toast => {
      setToasts(toasts => [...toasts, toast]);
    });

    return () => {
      toastTopicSub.dispose()
    };
  }, []);

  const closeToast = (toastId) => {
    const newToasts = toasts.filter(t => t.id !== toastId);
    setToasts(newToasts);
  };

  return (
    <div className="toast-container">
      {toasts.map(t =>
        <Toast key={`toast-${t.id}`} onClose={() => closeToast(t.id)} delay={3000} autohide>
          <Toast.Header>
            <strong className="mr-auto">{t.header}</strong>
          </Toast.Header>
          <Toast.Body>{t.content}</Toast.Body>
        </Toast>
      )}
    </div>
  )

};

