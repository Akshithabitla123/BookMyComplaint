// Get complaintId from URL
const params = new URLSearchParams(window.location.search);
const complaintId = params.get("id");

if (!complaintId) {
    alert("Invalid complaint ID");
}

// Fetch complaint details
fetch(`http://localhost:8080/complaints/admin/details/${complaintId}`)
    .then(res => res.json())
    .then(data => {
        console.log("Details:", data);

        document.getElementById("userName").innerText = data.userName;
        document.getElementById("email").innerText = data.email;
        document.getElementById("role").innerText = data.role;

        document.getElementById("title").innerText = data.title;
        document.getElementById("description").innerText = data.description;
        document.getElementById("area").innerText = data.area;
        document.getElementById("status").innerText = data.status;
        document.getElementById("assignedStaff").innerText =
            data.assignedStaff ? data.assignedStaff : "Not Assigned";

        document.getElementById("createdAt").innerText = formatDate(data.createdAt);
        document.getElementById("updatedAt").innerText = formatDate(data.updatedAt);
    })
    .catch(err => {
        console.error(err);
        alert("Failed to load complaint details");
    });

function formatDate(dateStr) {
    if (!dateStr) return "N/A";
    return new Date(dateStr).toLocaleString();
}


