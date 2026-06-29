document.addEventListener("DOMContentLoaded", () => {
    const params = new URLSearchParams(window.location.search);
    const complaintId = params.get("id");

    if (!complaintId) return;

    fetch(`http://localhost:8080/complaints/admin/details/${complaintId}`)
        .then(res => res.json())
        .then(data => {
            // SAFE DATA MAPPING - If an ID is missing in HTML, it won't crash the script
            const updateText = (id, value) => {
                const el = document.getElementById(id);
                if (el) el.innerText = value || "N/A";
            };

            updateText("userName", data.userName);
            updateText("email", data.email);
            updateText("role", data.role);
            updateText("title", data.title);
            updateText("description", data.description);
            updateText("area", data.area);
            updateText("status", data.status);
            updateText("assignedStaff", data.assignedStaff || "Not Assigned");

            // FORMAT DATES
            const createdAt = data.createdAt ? new Date(data.createdAt).toLocaleString() : "N/A";
            updateText("createdAt", createdAt);
            const updatedAt = data.updatedAt ? new Date(data.updatedAt).toLocaleString() : "N/A";
            updateText("updatedAt", updatedAt);

            // CALL THE DROPDOWN LOGIC
            setupStatusDropdown(data.status,complaintId);
        })
        .catch(err => console.error("Load Error:", err));
});

// Setup dropdown based on current status
function setupStatusDropdown(currentStatus,complaintId) {
    const select = document.getElementById("statusSelect");
    if (!select) return;

    // Normalize string to handle any casing from API
    const status = currentStatus ? currentStatus.toUpperCase().trim() : "";

    const existingBtn=document.getElementById("reviewBtn");
    if(existingBtn) existingBtn.remove();
    
    if(status==="ESCALATED"){
        //escalated complaints get review button
        select.style.display="none";
        const reviewBtn=document.createElement("button");
        reviewBtn.id="reviewBtn";
        reviewBtn.textContent="Review Escalation";
        reviewBtn.onclick=()=>reviewEscalation(complaintId);
        select.parentNode.appendChild(reviewBtn);
        return;
    }
    select.innerHTML = "";
    let options = [];

    if (status === "OPEN") {
        options = ["IN_PROGRESS", "REJECTED"];
    } else if (status === "IN_PROGRESS") {
        options = ["RESOLVED", "REJECTED"];
    } else {
        // If status is RESOLVED or REJECTED, hide the dropdown
        select.style.display = "none";
        return;
    }

    // Ensure it's visible if we have options
    select.style.display = "inline-block";

    const placeholder = document.createElement("option");
    placeholder.textContent = "Escalate Status";
    placeholder.disabled = true;
    placeholder.selected = true;
    select.appendChild(placeholder);

    options.forEach(opt => {
        const o = document.createElement("option");
        o.value = opt;
        o.textContent = opt.replace("_", " ");
        select.appendChild(o);
    });
}

//review escalation
function reviewEscalation(complaintId){
    fetch(`http://localhost:8080/complaints/admin/${complaintId}/review`,{
        method:"PUT"
    })
    .then(res=>{
        if(!res.ok){
            return res.json().then(data=>{
                throw new Error(data.error || "Review failed");
            });
        }
        return res.json();
    })
    .then(updated=>{
        document.getElementById("status").innerText=updated.status;
        setupStatusDropdown(updated.status,complaintId);
        alert("Complaint reviewed- now IN_PROGRESS");
    })
    .catch(err=>alert(err.message));
}

// Global function for the onchange event
window.changeStatus = function () {
    const select = document.getElementById("statusSelect");
    const newStatus = select.value;
    const complaintId = new URLSearchParams(window.location.search).get("id");

    if (!newStatus) return;

    fetch(`http://localhost:8080/complaints/admin/${complaintId}/status?status=${newStatus}`, {
        method: "PUT"
    })
    .then(res => {
        if (!res.ok) throw new Error("Update failed");
        return res.json();
    })
    .then(updated => {
        document.getElementById("status").innerText = updated.status;
        setupStatusDropdown(updated.status); // Refresh options
        alert("Status updated successfully!");
    })
    .catch(err => alert(err.message));
};