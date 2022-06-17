import React from "react";
import { Accordion } from "react-bootstrap";

const AccordionUsers = ({ elem, index, deleteUser, accessToken }) => {
   const userRoleName = elem.roleName.slice(5);

   return (
      <Accordion.Item eventKey={index}>
         <Accordion.Header>
            <div className="users-number-style">
               {elem.name} {elem.surname}
            </div>
            <span
               className={`ml-1 badge bg-${
                  userRoleName === "admin" ? "danger" : "secondary rounded-pill font-weight-normal "
               } py-1 users-badge-align`}
            >
               {userRoleName}
            </span>
         </Accordion.Header>
         <Accordion.Body>
            <p>
               <span className="font-weight-bold">Email: </span>
               {elem.email}
            </p>
            <p>
               <span className="font-weight-bold">Phone Number: </span>
               {elem.phoneNumber}
            </p>
            <p>
               <span className="font-weight-bold">Address: </span>
               {elem.address}
            </p>
            <p>
               <span className="font-weight-bold">User ID: </span>
               {elem.id}
            </p>
            <div className="d-flex justify-content-between">
               <div>
                  {/* This button is still a placeholder, functinality will be added */}
                  <button className="btn btn-info text-white">Promote</button>
                  <button className="btn btn-primary ml-2">Demote</button>
               </div>
               <div>
                  <button
                     onClick={() => {
                        deleteUser(accessToken, elem.id);
                     }}
                     className="btn btn-danger"
                  >
                     Delete
                  </button>
               </div>
            </div>
         </Accordion.Body>
      </Accordion.Item>
   );
};

export default AccordionUsers;
