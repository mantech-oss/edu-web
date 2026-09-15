const $ = (id) => document.getElementById(id);

async function loadInfo() {
  try {
    const response = await fetch('api/info', { cache: 'no-store' });
    if (!response.ok) throw new Error(`HTTP ${response.status}`);
    const info = await response.json();

    $('domain').textContent = info.domain;
    $('domainDetail').textContent = info.domain;
    $('namespace').textContent = info.namespace;
    $('podName').textContent = info.podName;
    $('podNameDetail').textContent = info.podName;
    $('podIp').textContent = info.podIp;
    $('podIpDetail').textContent = info.podIp;
    $('volumePath').textContent = info.volumePath;
    $('filePath').textContent = `${info.volumePath}/k8s-training-info.txt`;
  } catch (error) {
    $('saveStatus').className = 'save-status error';
    $('saveStatus').textContent = `정보 조회 실패: ${error.message}`;
  }
}

async function saveInfo() {
  const button = $('saveBtn');
  button.disabled = true;
  $('saveStatus').className = 'save-status idle';
  $('saveStatus').textContent = '현재 정보를 /mnt에 저장하는 중...';

  try {
    const response = await fetch('api/save', { method: 'POST' });
    const result = await response.json();
    if (!response.ok || !result.success) throw new Error(result.message || `HTTP ${response.status}`);

    $('saveStatus').className = 'save-status success';
    $('saveStatus').textContent = `✓ 저장 완료: ${result.file}`;
    await viewSavedFile();
  } catch (error) {
    $('saveStatus').className = 'save-status error';
    $('saveStatus').textContent = `저장 실패: ${error.message}`;
  } finally {
    button.disabled = false;
  }
}

async function viewSavedFile() {
  $('savedPanel').classList.remove('hidden');
  $('savedContent').textContent = '저장 파일을 읽는 중...';

  try {
    const response = await fetch('api/saved', { cache: 'no-store' });
    const text = await response.text();
    if (!response.ok) throw new Error(text || `HTTP ${response.status}`);
    $('savedContent').textContent = text;
  } catch (error) {
    $('savedContent').textContent = error.message;
  }

  $('savedPanel').scrollIntoView({ behavior: 'smooth', block: 'nearest' });
}

$('refreshBtn').addEventListener('click', loadInfo);
$('saveBtn').addEventListener('click', saveInfo);
$('viewBtn').addEventListener('click', viewSavedFile);
$('closeSavedBtn').addEventListener('click', () => $('savedPanel').classList.add('hidden'));
loadInfo();
