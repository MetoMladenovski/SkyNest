import React, { useState } from "react";
import { Modal } from "react-bootstrap";
import * as AiCions from "react-icons/ai";
import AxiosInstance from "../../axios/AxiosInstance";
import SetSuccessMsg from "../../ReusableComponents/SetSuccessMsg";
import SetErrorMsg from "../../ReusableComponents/SetErrorMsg";

const AddBucketModal = ({ refreshBuckets }) => {
   const [show, setShow] = useState(false);
   const [name, setName] = useState("");
   const [description, setDescription] = useState("");
   const [errorMsg, setErrorMsg] = useState("");
   const [successMsg, setSuccessMsg] = useState("");
   const [loading, setLoading] = useState(false);

   const handleClose = () => setShow(false);
   const handleShow = () => setShow(true);
   const accessToken = localStorage.accessToken;

   const createNewBucket = async () => {
      try {
         await AxiosInstance.post(
            "/buckets",
            {
               name,
               description,
            },
            { headers: { Authorization: accessToken } }
         );
         setSuccessMsg("Bucket Created");
         setTimeout(() => {
            setShow(false);
            setName("");
            setDescription("");
            refreshBuckets();
         }, 2000);
      } catch (err) {
         if (err.response.status === 400) {
            setErrorMsg("Inputs can't be empty");
         } else {
            setErrorMsg(err.response.data.messages);
            console.log(err);
         }
      }
   };

   const onFormSubmit = async (e) => {
      e.preventDefault();
      setLoading(true);
      await createNewBucket();
      setLoading(false);
   };

   return (
      <>
         <span onClick={handleShow} className="ml-1 latte-background custom-rounded">
            <AiCions.AiOutlinePlusCircle className="main-icon-align" /> Create Bucket
         </span>

         <Modal show={show} onHide={handleClose} className="mt-3">
            <Modal.Body>
               <SetErrorMsg errorMsg={errorMsg} setErrorMsg={setErrorMsg} customStyle="m-0 w-100 alert alert-danger text-danger text-center mb-3" />
               <SetSuccessMsg
                  successMsg={successMsg}
                  setSuccessMsg={setSuccessMsg}
                  customStyle="m-0 w-100 alert alert-success text-success text-center mb-3"
               />
               <form onSubmit={onFormSubmit}>
                  <fieldset disabled={loading}>
                     <div className="form-group row">
                        <label htmlFor="nameInp" className="col-sm-3 col-form-label">
                           Name:
                        </label>
                        <div className="col-sm-9">
                           <input value={name} onChange={(e) => setName(e.target.value)} className="form-control" id="nameInp" placeholder="Name" />
                        </div>
                     </div>
                     <div className="form-group row">
                        <label htmlFor="descrInp" className="col-sm-3 col-form-label">
                           Description:
                        </label>
                        <div className="col-sm-9">
                           <input
                              value={description}
                              onChange={(e) => setDescription(e.target.value)}
                              className="form-control"
                              id="descrInp"
                              placeholder="Description"
                           />
                        </div>
                     </div>
                     <div className="mt-4 d-flex justify-content-end">
                        <button className="btn btn-secondary button-width">Create</button>
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

export default AddBucketModal;
