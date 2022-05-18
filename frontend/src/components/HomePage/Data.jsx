import React from "react";

const Data = ({ fakeData }) => {
	const allFakeData = fakeData.map((data) => {
		let color = "success";
		if (data.size >= 200 && data.size < 400) {
			color = "primary";
		} else if (data.size >= 400 && data.size < 600) {
			color = "warning";
		} else if (data.size >= 600) {
			color = "danger";
		}

		return (
			<div key={data.id} className={`d-inline-block m-3 card border-${color} mb-3`} style={{ maxWidth: "12rem" }}>
				{/* <div className="card-header border-success">File number: {data.id}</div> */}
				<div className={`card-body text-${color}`}>
					<h5 className="card-title">{data.title}</h5>
					<p className="card-text">{data.info}</p>
				</div>
				<div className={`card-footer bg-transparent border-${color}`}>{data.size} mb</div>
			</div>
		);
	});

	return <div>{allFakeData}</div>;
};

export default Data;
