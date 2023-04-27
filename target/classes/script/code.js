let data = {
    'al': 1,
    'act': 'section',
    'claim': 0,
    'is_layer': 0,
    'owner_id': 718258940,
    'section': 'search',
    'q': q
}

const response = await fetch("/al_audio.php?act=reload_audios", {
    method: "POST", // or 'PUT'
    headers: {
        "Content-Type": "application/json",
    },
    body: JSON.stringify(data),
});

const jsonData = await response.json();
console.log(jsonData);


