import React, {useEffect, useState} from "react";
import {mkTopic} from "util/topic";
import Modal from "react-bootstrap/Modal";
import Button from "react-bootstrap/Button";


const dialogTopic = mkTopic();

export const showConfirmation = (message, onConfirm, onCancel) => {
  dialogTopic.publish({
    message,
    onConfirm,
    onCancel
  });
}

export const ConfirmationDialog = () => {

  const [dialogInfo, setDialogInfo] = useState();

  useEffect(() => {
    const dialogTopicSub = dialogTopic.subscribe(dialog => {
      setDialogInfo(dialog);
    });

    return () => {
      dialogTopicSub.dispose()
    };
  }, []);

  const onCancel = () => {
    setDialogInfo();
    dialogInfo.onCancel();
  };

  const onConfirm = () => {
    setDialogInfo();
    dialogInfo.onConfirm();
  };

  return (
    <div className="confirmation-dialog-container">
      <Modal show={!!dialogInfo} onHide={onCancel}>
        <Modal.Header closeButton>
          <Modal.Title>Confirmation</Modal.Title>
        </Modal.Header>
        <Modal.Body>{dialogInfo && dialogInfo.message}</Modal.Body>
        <Modal.Footer>
          <Button variant="secondary" onClick={onCancel}>Cancel</Button>
          <Button variant="primary" onClick={onConfirm}>Ok</Button>
        </Modal.Footer>
      </Modal>
    </div>
  );

};
