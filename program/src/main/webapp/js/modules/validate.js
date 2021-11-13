const patternEndpoint = "./api/v1/validationpattern/regex/";

function loadPattern(key) {
    const xmlHttp = new XMLHttpRequest();
    const idEndpoint = patternEndpoint + key;
    xmlHttp.open( "GET", idEndpoint, false ); // false for synchronous request
    xmlHttp.send( null);
    const result = JSON.parse(xmlHttp.responseText);
    if (!result || !result.hasOwnProperty("value")) return undefined;
    return result["value"];
}

function markInvalid(target) {
    if (!target) return;
    if (target.classList.contains("valid")) {
        target.classList.remove("valid");
    }
    target.classList.add("invalid");
}

function markValid(target) {
    if (!target) return;
    if (target.classList.contains("invalid")) {
        target.classList.remove("invalid");
    }
    target.classList.add("valid");
}

function reset() {
    sessionStorage.clear();
}

function handleKey(key) {
    let result = sessionStorage.getItem(key);
    if (result === undefined || result == null) {
        result = loadPattern(key);
        sessionStorage.setItem(key, result)
    }
    if (result === undefined) return;
    const regEx = RegExp(result);
    const input = event.target.value;
    if (regEx.test(input)) {
        markValid(event.target);
    } else {
        markInvalid(event.target);
    }
}

function update() {
    document.querySelectorAll(".name").forEach((item) => {
        item.addEventListener("input", event => {
            handleKey("Name");
        });
    });

    document.querySelectorAll(".carrier-id").forEach((item) => {
        item.addEventListener("input", event => {
            handleKey("CarrierId");
        });
    });

    document.querySelectorAll(".carrier-type").forEach((item) => {
        item.addEventListener("input", event => {
            handleKey("CarrierType");
        });
    });

    document.querySelectorAll(".job-name").forEach((item) => {
        item.addEventListener("input", event => {
            handleKey("JobName");
        });
    });

    document.querySelectorAll(".state-name").forEach((item) => {
        item.addEventListener("input", event => {
            handleKey("StateName");
        });
    });

    document.querySelectorAll(".state-machine-name").forEach((item) => {
        item.addEventListener("input", event => {
            handleKey("StateMachineName");
        });
    });

    document.querySelectorAll(".workstation-name").forEach((item) => {
        item.addEventListener("input", event => {
            handleKey("WorkstationName");
        });
    });

    document.querySelectorAll(".process-step-name").forEach((item) => {
        item.addEventListener("input", event => {
            handleKey("ProcessStepName");
        });
    });

    document.querySelectorAll(".process-chain-name").forEach((item) => {
        item.addEventListener("input", event => {
            handleKey("ProcessChainName");
        });
    });

    document.querySelectorAll(".email").forEach((item) => {
        item.addEventListener("input", event => {
            handleKey("Email");
        });
    });

    document.querySelectorAll(".password").forEach((item) => {
        item.addEventListener("input", event => {
            handleKey("Password");
        });
    });

    document.querySelectorAll(".username").forEach((item) => {
        item.addEventListener("input", event => {
            handleKey("Username");
        });
    });

    document.querySelectorAll(".assembly-alloy").forEach((item) => {
        item.addEventListener("input", event => {
            handleKey("AssemblyAlloy");
        });
    });

    document.querySelectorAll(".assembly-comment").forEach((item) => {
        item.addEventListener("input", event => {
            handleKey("AssemblyComment");
        });
    });

    document.querySelectorAll(".assembly-id").forEach((item) => {
        item.addEventListener("input", event => {
            handleKey("AssemblyId");
        });
    });

    document.querySelectorAll(".position").forEach((item) => {
        item.addEventListener("input", event => {
            handleKey("Position");
        });
    });

    document.querySelectorAll(".parameter-field").forEach((item) => {
        item.addEventListener("input", event => {
            handleKey("ParameterField");
        });
    });

    document.querySelectorAll(".priority-name").forEach((item) => {
        item.addEventListener("input", event => {
            handleKey("PriorityName");
        });
    });

    document.querySelectorAll(".value").forEach((item) => {
        item.addEventListener("input", event => {
            handleKey("Value");
        });
    });

    document.querySelectorAll(".id").forEach((item) => {
        item.addEventListener("input", event => {
            handleKey("Id");
        });
    });

    document.querySelectorAll(".integer").forEach((item) => {
        item.addEventListener("input", event => {
            handleKey("Integer");
        });
    });
}

$(document).ready(function () {
    update();
});
