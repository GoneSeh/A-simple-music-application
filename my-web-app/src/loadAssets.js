// src/loadAssets.js

export function loadScript(url) {
  return new Promise((resolve, reject) => {
    // If the script is already present, resolve immediately
    if (document.querySelector(`script[src="${url}"]`)) {
      return resolve();
    }
    const script = document.createElement("script");
    script.src = url;
    script.async = true;
    script.onload = () => resolve();
    script.onerror = () => reject(new Error(`Failed to load script: ${url}`));
    document.body.appendChild(script);
  });
}

export function loadStyle(url) {
  return new Promise((resolve, reject) => {
    // If the stylesheet is already present, resolve immediately
    if (document.querySelector(`link[href="${url}"]`)) {
      return resolve();
    }
    const link = document.createElement("link");
    link.href = url;
    link.rel = "stylesheet";
    link.onload = () => resolve();
    link.onerror = () => reject(new Error(`Failed to load stylesheet: ${url}`));
    document.head.appendChild(link);
  });
}
