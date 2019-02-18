package nexusvault.archive.impl;

import nexusvault.archive.IdxPath;

final class BaseRootIdxDirectory extends BaseIdxDirectory {

	private BaseNexusArchive archive;

	public BaseRootIdxDirectory(int headerIndex) {
		super(null, "", headerIndex);
	}

	@Override
	public String getFullName() {
		return getName();
	}

	@Override
	public IdxPath getPath() {
		return new BaseIdxPath();
	}

	@Override
	public BaseNexusArchive getArchive() {
		return archive;
	}

	protected void setArchive(BaseNexusArchive archive) {
		this.archive = archive;
	}

	@Override
	public String toString() {
		final StringBuilder builder = new StringBuilder("IdxRootDirectory [");
		builder.append("headerIdx=").append(getDirectoryPackIndex());
		builder.append(", #childs=").append(getChilds().size());
		builder.append("]");
		return builder.toString();
	}

}
