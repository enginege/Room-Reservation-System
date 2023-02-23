window.onload = function() {
    setTable();
    sortTableByDate(0);
    sortTableByDate(0);

};

function setTable() {
    const styleSheet = document.createElement('style')
    styleSheet.innerHTML = `
        .inactive span {
            visibility:hidden;
        }
        .inactive:hover span {
            visibility:visible;
        }
        .active span {
            visibility: visible;
        }
    `
    document.head.appendChild(styleSheet)

    document.querySelectorAll('th.order').forEach(TH => {
        const span_elem = document.createElement('span')
        span_elem.style = "font-size:0.8rem; margin-left:0.5rem"
        span_elem.innerHTML = "▲"
        TH.appendChild(span_elem)
        TH.classList.add('inactive')
    });
}



function sortTable(n) {
    var th_elem = document.getElementsByTagName("TH")[n];
    var table, rows, switching, i, x, y, shouldSwitch, dir, switchcount = 0;
    table = document.getElementById("Table");
    switching = true;
    dir = "asc";

    document.querySelectorAll("th.order").forEach(elem => {
        elem.classList.remove('active')
        elem.classList.add('inactive')
    })
    th_elem.classList.remove('inactive')
    th_elem.classList.add('active');

    while (switching) {
        th_elem.classList.remove('inactive')
        th_elem.classList.add('active')
        if (dir === "asc") {
            th_elem.querySelector('span').innerHTML = '▲'
        } else {
            th_elem.querySelector('span').innerHTML = '▼'
        }
        switching = false;
        rows = table.rows;
        for (i = 1; i < (rows.length - 1); i++) {
            shouldSwitch = false;
            x = rows[i].getElementsByTagName("TD")[n];
            y = rows[i + 1].getElementsByTagName("TD")[n];
            if (dir == "asc") {
                if (x.innerHTML.toLowerCase() > y.innerHTML.toLowerCase()) {
                    shouldSwitch = true;
                    break;
                }
            } else if (dir == "desc") {
                if (x.innerHTML.toLowerCase() < y.innerHTML.toLowerCase()) {
                    shouldSwitch = true;
                    break;
                }
            }
        }
        if (shouldSwitch) {
            rows[i].parentNode.insertBefore(rows[i + 1], rows[i]);
            switching = true;
            switchcount ++;
        } else {
            if (switchcount == 0 && dir == "asc") {
                dir = "desc";
                switching = true;
            }
        }
    }
}

function sortTableNumerically(n) {
    var th_elem = document.getElementsByTagName("TH")[n];
    var table, rows, switching, i, x, y, shouldSwitch, dir, switchcount = 0;
    table = document.getElementById("Table");
    switching = true;
    dir = "asc";

    document.querySelectorAll("th.order").forEach(elem => {
        elem.classList.remove('active')
        elem.classList.add('inactive')
    })
    th_elem.classList.remove('inactive')
    th_elem.classList.add('active');

    while (switching) {
        th_elem.classList.remove('inactive')
        th_elem.classList.add('active')
        if (dir === "asc") {
            th_elem.querySelector('span').innerHTML = '▲'
        } else {
            th_elem.querySelector('span').innerHTML = '▼'
        }
        switching = false;
        rows = table.rows;
        for (i = 1; i < (rows.length - 1); i++) {
            shouldSwitch = false;
            x = rows[i].getElementsByTagName("TD")[n];
            y = rows[i + 1].getElementsByTagName("TD")[n];
            if (dir == "asc") {
                if (Number(x.innerHTML) > Number(y.innerHTML)) {
                    shouldSwitch = true;
                    break;
                }
            } else if (dir == "desc") {
                if (Number(x.innerHTML) < Number(y.innerHTML)) {
                    shouldSwitch = true;
                    break;
                }
            }
        }
        if (shouldSwitch) {
            rows[i].parentNode.insertBefore(rows[i + 1], rows[i]);
            switching = true;
            switchcount ++;
        } else {
            if (switchcount == 0 && dir == "asc") {
                dir = "desc";
                switching = true;
            }
        }
    }
}

function strToDate(dtStr) {
    if (!dtStr) return null
    let dateParts = dtStr.split("-");
    let timeParts = dateParts[2].split(" ")[1].split(":");
    dateParts[2] = dateParts[2].split(" ")[0];
    const date = new Date(+dateParts[2], dateParts[1] - 1, +dateParts[0], timeParts[0], timeParts[1]);
    // console.log(date);
    return date;
}

function sortTableByDate(n) {
    var th_elem = document.getElementsByTagName("TH")[n];
    var table, rows, switching, i, x, y, shouldSwitch, dir, switchcount = 0;
    table = document.getElementById("Table");
    switching = true;
    dir = "asc";

    document.querySelectorAll("th.order").forEach(elem => {
        elem.classList.remove('active')
        elem.classList.add('inactive')
    })
    th_elem.classList.remove('inactive')
    th_elem.classList.add('active');

    while (switching) {
        th_elem.classList.remove('inactive')
        th_elem.classList.add('active')
        if (dir === "asc") {
            th_elem.querySelector('span').innerHTML = '▲'
        } else {
            th_elem.querySelector('span').innerHTML = '▼'
        }
        switching = false;
        rows = table.rows;
        for (i = 1; i < (rows.length - 1); i++) {
            shouldSwitch = false;
            x = rows[i].getElementsByTagName("TD")[n];
            y = rows[i + 1].getElementsByTagName("TD")[n];
            if (dir == "asc") {
                if (strToDate(x.innerHTML) > strToDate(y.innerHTML)) {
                    shouldSwitch = true;
                    break;
                }
            } else if (dir == "desc") {
                if (strToDate(x.innerHTML) < strToDate(y.innerHTML)) {
                    shouldSwitch = true;
                    break;
                }
            }
        }
        if (shouldSwitch) {
            rows[i].parentNode.insertBefore(rows[i + 1], rows[i]);
            switching = true;
            switchcount ++;
        } else {
            if (switchcount == 0 && dir == "asc") {
                dir = "desc";
                switching = true;
            }
        }
    }
}

var x = 0;

const searchInput = document.getElementById('searchInput')
const searchFilter = document.getElementById('search-filter');

searchFilter.addEventListener('change', () => {
    var index = searchFilter.options[searchFilter.selectedIndex].value;
    // console.log(index);
    var filter = searchFilter.options[searchFilter.selectedIndex].text;
    searchInput.setAttribute("placeholder", ` ${filter}`);
    x = index;
    searchTable();
});

function searchTable() {
    var input, filter, table, tr, td, i, txtValue;
    input = document.getElementById("searchInput");
    filter = input.value.toUpperCase();
    table = document.getElementById("Table");
    tr = table.getElementsByTagName("tr");



    for (i = 0; i < tr.length; i++) {
        td = tr[i].getElementsByTagName("td")[x];
        if (td) {
            txtValue = td.textContent || td.innerText;
            if (txtValue.toUpperCase().indexOf(filter) > -1) {
                tr[i].style.display = "";
            } else {
                tr[i].style.display = "none";
            }
        }
    }
}

function conflictingReservations(reservation_id) {

    $.post("/reservation/conflicts/" + reservation_id ,
        function( data ) {
            $("#ConflictsModal").append(data);
            $("#ConflictsModal").load(location.href + " #ConflictsModal>*","", function () {
                // setTable();
                // sortTableByDate(0);
                // sortTableByDate(0);
                document.getElementById('ConflictsButton').click();
            });

    })
}

function innerconflictingReservations(reservation_id) {
    document.getElementById('ConflictsModalClose').click();
    $.post("/reservation/conflicts/" + reservation_id ,
        function( data ) {
            $("#ConflictsModal").append(data);
            $("#ConflictsModal").load(location.href + " #ConflictsModal>*","", function () {
                // setTable();
                // sortTableByDate(0);
                // sortTableByDate(0);
                document.getElementById('ConflictsButton').click();
            });

    })
}

function approveReservation(id) {

    $.post("/reservation/approve/" + id ,
        function( data ) {
            $("#Table").append(data);
            $("#Table").load(location.href + " #Table>*","", function () {
                document.getElementById('ConflictsModalClose').click();
                setTable();
                sortTableByDate(0);
                sortTableByDate(0);
            });

        })
}

function declineReservation(id) {

    $.post("/reservation/decline/" + id ,
        function( data ) {
            $("#Table").append(data);
            $("#Table").load(location.href + " #Table>*","", function () {
                document.getElementById('ConflictsModalClose').click();
                setTable();
                sortTableByDate(0);
                sortTableByDate(0);
            });

        })
}

function revokeReservation(id) {

    $.post("/reservation/revoke/" + id ,
        function( data ) {
            $("#Table").append(data);
            $("#Table").load(location.href + " #Table>*","", function () {
                setTable();
                sortTableByDate(0);
                sortTableByDate(0);
            });

        })
}


function finishReservation(id) {

    $.post("/reservation/finish/" + id ,
        function( data ) {
            $("#Table").append(data);
            $("#Table").load(location.href + " #Table>*","", function () {
                setTable();
                sortTableByDate(0);
                sortTableByDate(0);
            });

        })
}

function resubmitReservation(id) {

    $.post("/reservation/resubmit/" + id ,
        function( data ) {
            var error_CurrentDate = document.getElementById('error_CurrentDate');
            var error_Conflict = document.getElementById('error_Conflict');
            var success = document.getElementById('success');

        if (data.at(0).name === "error_CurrentDate") {
            error_CurrentDate.style.display = "block";
            $("#error_CurrentDate").fadeOut(4000, function (){
                error_CurrentDate.setAttribute("display", "none");
            });
        }
        else if (data.at(0).name === "error_Conflict") {
            error_Conflict.style.display = "block";
            $("#error_Conflict").fadeOut(4000, function (){
                // error_Conflict.setAttribute("display", "none");
            });
        }
        else {
            $("#Table").append(data);
            $("#Table").load(location.href + " #Table>*","", function () {
                setTable();
                sortTableByDate(0);
                sortTableByDate(0);
                success.style.display = "block";
                $("#success").fadeOut(4000, function (){
                    success.setAttribute("display", "none");
                });

            });
        }
        })
}

function deleteReservation(id) {

    $.post("/reservation/delete/" + id ,
        function( data ) {
            $("#Table").append(data);
            $("#Table").load(location.href + " #Table>*","", function () {
                setTable();
                sortTableByDate(0);
                sortTableByDate(0);
            });

        })
}

