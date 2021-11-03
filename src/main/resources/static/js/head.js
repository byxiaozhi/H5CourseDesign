/* 页面加载前执行 */

function isDarkMode() {
    return localStorage.darkMode === "1";
}

function switchDarkMode(dark) {
    if (dark) {
        const link = document.createElement("link");
        link.id = "theme-css-dark";
        link.rel = "stylesheet";
        link.type = "text/css";
        link.href = "/css/theme-dark.css";
        document.querySelector("head").appendChild(link);
        localStorage.setItem("darkMode", "1");
    } else {
        document.querySelector("#theme-css-dark").remove();
        localStorage.setItem("darkMode", "0");
    }
}

isDarkMode() && switchDarkMode(true);

// 获取URL参数
function getUrlKey(name) {
    return decodeURIComponent((new RegExp(`[?|&]${name}=([^&;]+?)(&|#|;|$)`).exec(window.location.href) || ["", ""])[1].replace(/\+/g, '%20'));
}

