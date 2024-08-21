let hocon = window.document.getElementById('hocon');
let output = window.document.getElementById('output');

let inputResolve = window.document.getElementById('resolve');
let inputJson = window.document.getElementById('json');
let inputFormatted = window.document.getElementById('formatted');
let inputComments = window.document.getElementById('comments');
let inputOriginComments = window.document.getElementById('originComments');

hocon.addEventListener('keyup', updateOutput);

inputResolve.addEventListener('input', updateOutput)
inputJson.addEventListener('input', updateOutput)
inputFormatted.addEventListener('input', updateOutput)
inputComments.addEventListener('input', updateOutput)
inputOriginComments.addEventListener('input', updateOutput)

function updateOutput() {
    const query =
        `resolve=${inputResolve.checked}` +
        `&json=${inputJson.checked}` +
        `&formatted=${inputFormatted.checked}` +
        `&comments=${inputComments.checked}` +
        `&originComments=${inputOriginComments.checked}`;
    fetch(`/hoconToJson?${query}`, {
        method: 'POST',
        body: hocon.value,
    }).then(r => r.text()).then(renderedJson => {
        output.textContent = renderedJson;
    });
}

updateOutput();
