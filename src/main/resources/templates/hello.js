const admin = JSON.parse(localStorage.getItem("loggedInAdmin"));
//<!--    if(!admin){-->
//<!--        alert("Admin login required");-->
//<!--        window.location.href = "admin_login.html";-->
//<!--    }-->

    loadComplaints();

    function loadComplaints(){
        fetch("http://localhost:8080/complaints/admin/complaints")
            .then(res => res.json())
            .then(data => renderTable(data))
            .catch(err => alert("Failed to load complaints"));
    }

    function renderTable(data){
        const table = document.getElementById("complaintTable");
        table.innerHTML = `
            <tr>
                <th>ID</th>
                <th>Title</th>
                <th>Area</th>
                <th>Status</th>
                <th>Assigned Staff</th>
                <th>Assign Staff</th>
            </tr>
        `;

        data.forEach(c => {
            const row=document.createElement("tr");
            row.innerHTML += `
                    <td>${c.complaintId}</td>
                    <td>${c.title}</td>
                    <td>${c.area}</td>
                    <td>${c.status}</td>
                    <td>${c.assignedStaff ? c.assignedStaff : "Not Assigned"}</td>
                    <td>
                        ${
                            c.assignedStaff?"":`<input type="text" id="staff-${c.complaintId}" placeholder="Staff Name"> <button onclick="assignStaff(${c.complaintId})">Assign</button>`
                        }

                    </td>
            `;
            table.appendChild(row);
        });
    }


    //on filter change

    function onFilterChange(){
        const filter=document.getElementById("filterType").value;

        document.getElementById("titleInput").style.display="none";
        document.getElementById("areaInput").style.display="none";
        document.getElementById("statusInput").style.display="none";

        if(filter=="title"){
            document.getElementById("titleInput").style.display="inline-block";
        }
        else if(filter=="area"){
            document.getElementById("areaInput").style.display="inline-block";
        }
        else if(filter=="status"){
            document.getElementById("statusInput").style.display="inline-block";
        }


    }
    // Assign staff
    function assignStaff(complaintId){
        const staffName = document.getElementById(`staff-${complaintId}`).value;

        if(!staffName){
            alert("Enter staff Name");
            return;
        }

        fetch(`http://localhost:8080/complaints/admin/${complaintId}/assign?staffName=${encodeURIComponent(staffName)}`, {
            method: "PUT"
        })
        .then(res => {
            if(res.ok){
                alert("Staff assigned");
                loadComplaints();
            }else{
                alert("Assignment failed");
            }
        })
        .catch(error=>{
            console.error("Error: ",error);
            alert("server error");
          });
    }

//<!--    Apply filters-->
    function applyFilter(){
        const filterType = document.getElementById("filterType").value;
            let value = "";

            if (filterType === "title") {
                value = document.getElementById("titleInput").value;
            }
            else if (filterType === "area") {
                value = document.getElementById("areaInput").value;
            }
            else if (filterType === "status") {
                value = document.getElementById("statusInput").value;
            }

            if(!value){
                alert("Please enter a value");
                return;
            }

        const url = `http://localhost:8080/complaints/admin/filter/${filterType}?${filterType}=${encodeURIComponent(value)}`;


        fetch(url)
            .then(res => res.json())
            .then(data => renderTable(data))
            .catch(err => alert("Filter failed"));
    }
    function logout(){
//<!--        local storage is per browser per domain-->
        localStorage.removeItem("loggedInAdmin");
        window.location.href = "index.html";
    }