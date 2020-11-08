function detectMob() {
    if ((window.innerWidth < 1000) || (window.innerHeight < 500)){
        window.location.href = "https://notes.inbarkoursh.com/unsupported";
    }
}

detectMob();

window.addEventListener("resize", detectMob);