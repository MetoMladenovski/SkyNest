import React, { useState } from "react";
import { Modal } from "react-bootstrap";
import AxiosInstance from "../../axios/AxiosInstance";
import SetErrorMsg from "../../ReusableComponents/SetErrorMsg";
import SetSuccessMsg from "../../ReusableComponents/SetSuccessMsg";
import ModalLoader from "../../Loader/ModalLoader";

const EditFileInfo = ({ elem, refresh }) => {
   const [show, setShow] = useState(false);
   const [name, setName] = useState(elem.name);
   const [loading, setLoading] = useState(false);
   const [errorMsg, setErrorMsg] = useState("");
   const [successMsg, setSuccessMsg] = useState("");

   const handleClose = () => setShow(false);
   const handleShow = () => setShow(true);
   const accessToken = localStorage.accessToken;

   const editFile = async () => {
      try {
         await AxiosInstance.put(
            `files/${elem.id}/info`,
            { name },
            {
               headers: { Authorization: accessToken },
            }
         );
         setSuccessMsg("File Edited");
         setTimeout(() => {
            setShow(false);
            refresh();
         }, 2000);
      } catch (err) {
         if (err.response.status === 400) {
            console.log(err);
            setErrorMsg("Inputs can't be empty");
         } else {
            setErrorMsg(err.response.data.messages);
            console.log(err);
         }
      }
   };

   const onFormSubmit = async () => {
      setLoading(true);
      await editFile();
      setLoading(false);
   };

   return (
      <>
         <span onClick={handleShow} className="ml-0">
            Edit File
         </span>

         <Modal show={show} onHide={handleClose} className="mt-3">
            <Modal.Body>
               <SetErrorMsg errorMsg={errorMsg} setErrorMsg={setErrorMsg} customStyle="m-0 w-100 alert alert-danger text-danger text-center mb-3" />
               <SetSuccessMsg
                  successMsg={successMsg}
                  setSuccessMsg={setSuccessMsg}
                  customStyle="m-0 w-100 alert alert-success text-success text-center mb-3"
               />
               <form>
                  <fieldset disabled={loading}>
                     <div className="form-group row">
                        <label className="col-sm-3 col-form-label">Name:</label>
                        <div className="col-sm-9">
                           <input value={name} onChange={(e) => setName(e.target.value)} className="form-control" placeholder="Name" />
                        </div>
                     </div>
                     <div className="mt-4 d-flex justify-content-end">
                        {!loading ? (
                           <button onClick={onFormSubmit} className="btn btn-secondary button-width">
                              Edit
                           </button>
                        ) : (
                           <ModalLoader />
                        )}
                        <button
                           onClick={(e) => {
                              e.preventDefault();
                              handleClose();
                              setErrorMsg("");
                           }}
                           className="ml-2 btn btn-outline-secondary button-width"
                        >
                           Close
                        </button>
                     </div>
                  </fieldset>
               </form>
            </Modal.Body>
         </Modal>
      </>
   );
};

export default EditFileInfo;
