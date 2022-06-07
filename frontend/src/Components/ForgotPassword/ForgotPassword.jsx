import React, { useEffect, useRef, useState } from "react";
import ROUTES from "../Routes/ROUTES";
import { Link, useNavigate } from "react-router-dom";
import CenteredContainer from "../ReusableComponents/CenteredContainer";
import AxiosInstance from "../axios/AxiosInstance";
import { inputsDisabled } from "../ReusableComponents/ReusableFunctions";
import LoadingButton from "../Loader/LoadingButton";

const ForgotPassword = () => {
   const [email, setEmail] = useState("");
   const [errorMsg, setErorrMsg] = useState("");
   const [successMsg, setSuccessMsg] = useState("");
   const [loading, setLoading] = useState(false);

   const emailRef = useRef();
   const navigate = useNavigate();
   const allInputs = document.querySelectorAll("input");

   const redirectToLoginPage = (delay) => {
      setTimeout(() => {
         navigate(ROUTES.LOGIN);
      }, delay);
   };

   const onEmailChange = (e) => {
      setEmail(e.target.value);
   };

   const onFormSubmit = async (e) => {
      e.preventDefault();
      setLoading(true);
      inputsDisabled(allInputs, true);
      await onSuccessfulChange();
      inputsDisabled(allInputs, false);
   };

   const onSuccessfulChange = async () => {
      try {
         await AxiosInstance.post(`/users/password-reset/request?email=${email}`);
         setSuccessMsg("Email has been sent");
         redirectToLoginPage(2000);
      } catch ({ response }) {
         if (response.status === 400) {
            setErorrMsg("Email not found");
         }
         setLoading(false);
      }
   };

   useEffect(() => {
      emailRef.current.focus();
   }, []);

   useEffect(() => {
      setErorrMsg("");
   }, [email]);

   return (
      <CenteredContainer>
         <form onSubmit={onFormSubmit}>
            <h5 className="mb-4 p-0 text-center text-dark">Please enter your e-mail</h5>
            <div className="form-outline mb-4">
               <p className={errorMsg ? "alert alert-danger text-danger text-center" : "d-none"}>{errorMsg}</p>
               <p className={successMsg ? "alert alert-success text-success text-center" : "d-none"}>{successMsg}</p>
               <label className="form-label" htmlFor="emailInput">
                  Email address
               </label>
               <input
                  type="email"
                  onChange={onEmailChange}
                  value={email}
                  ref={emailRef}
                  id="emailInput"
                  className="form-control form-control-lg"
                  required
                  autoComplete="off"
               />
            </div>
            <div className="pt-1 mb-4">
               {loading ? (
                  <LoadingButton />
               ) : (
                  <div className="pt-1 mb-4">
                     <button className="btn btn-dark btn-lg btn-block">Submit</button>
                  </div>
               )}
            </div>
            <div className="mt-4 text-center">
               <p className="m-0">Wan't to go back? </p>
               <Link to={ROUTES.LOGIN} className="m-0 btn btn-link">
                  Login Page
               </Link>
            </div>
         </form>
      </CenteredContainer>
   );
};

export default ForgotPassword;
