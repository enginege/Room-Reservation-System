const startDate = document.getElementById('start_datetime');
const endDate = document.getElementById('end_datetime');

// let currentDate = new Date().toISOString().slice(0, -8); //yyyy-MM-ddThh:mm

//UTC to +03 TimeZone converter
function toIsoString(date) {
    var tzo = -date.getTimezoneOffset(),
        dif = tzo >= 0 ? '+' : '-',
        pad = function(num) {
            return (num < 10 ? '0' : '') + num;
        };

    return date.getFullYear() +
        '-' + pad(date.getMonth() + 1) +
        '-' + pad(date.getDate()) +
        'T' + pad(date.getHours()) +
        ':' + pad(date.getMinutes()) +
        ':' + pad(date.getSeconds()) +
        dif + pad(Math.floor(Math.abs(tzo) / 60)) +
        ':' + pad(Math.abs(tzo) % 60);
}

var dt = new Date();

//If the start datetime field isn't already filled due to a reloaded form caused by an erroneous reservation attempt. => Set it to current date/time.
if (startDate.value === ""){
    startDate.value =toIsoString(dt).slice(0,-9);
}

//Set min values for datetimepickers according to current date/time.
startDate.min =toIsoString(dt).slice(0,-9);
endDate.min = startDate.value;

//If start datetime value is adjusted => Also adjust the end datetime field accordingly.
startDate.addEventListener('input',  () => {
    const result = document.getElementById('end_datetime');
    result.min = startDate.value;
    if (startDate.value > result.value && result.value !== "")
    {
        result.value = startDate.value;
    }
});

// endDate.addEventListener('change', () => {
//     const result = document.getElementById('start_datetime');
//     result.max = endDate.value;
// });